package com.kotlin.walkthrough.artifacts.todo.server

import android.util.Log
import com.kotlin.walkthrough.artifacts.todo.server.request.Method
import com.kotlin.walkthrough.artifacts.todo.server.response.Response
import com.kotlin.walkthrough.artifacts.todo.server.response.Status
import com.kotlin.walkthrough.artifacts.todo.server.tempfiles.TempFileManager
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*

private const val BUFSIZE = 8192

class HTTPSession(
    private val httpServer: HTTPServer,
    private val tempFileManager: TempFileManager,
    private var inputStream: InputStream,
    private val outputStream: OutputStream
) {
    val headers: MutableMap<String, String> = HashMap()
    val parms: MutableMap<String, String> = HashMap()
    var method: Method? = null
    var uri: String? = null
    private var splitbyte = 0
    private var rlen = 0
    private val tmpBucket: RandomAccessFile?
        get () {
            try {
                val tempFile = tempFileManager.createTempFile()
                return RandomAccessFile(tempFile.name, "rw")
            } catch (e: Exception) {
                Log.d("HTTPSession", "Error: " + e.message)
            }
            return null
        }

    fun execute() {
        try {
            val buf = ByteArray(BUFSIZE)
            splitbyte = 0
            rlen = 0
            run {
                var read = inputStream.read(buf, 0, BUFSIZE)
                while (read > 0) {
                    rlen += read
                    splitbyte = findHeaderEnd(buf, rlen)
                    if (splitbyte > 0) break
                    read = inputStream.read(buf, rlen, BUFSIZE - rlen)
                }
            }

            if (splitbyte < rlen) {
                val splitInputStream = ByteArrayInputStream(buf, splitbyte, rlen - splitbyte)
                val sequenceInputStream = SequenceInputStream(splitInputStream, inputStream)
                inputStream = sequenceInputStream
            }

            parms.clear()
            headers.clear()

            val hin = BufferedReader(InputStreamReader(ByteArrayInputStream(buf, 0, rlen)))
            val pre: MutableMap<String, String> = HashMap()

            decodeHeader(hin, pre, parms, headers)
            method = Method.lookup(pre["method"])
            if (method == null)
                throw ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Syntax error.")

            uri = pre["uri"]

            // Ok, now do the serve()
            val r: Response? = httpServer.serve(this)
            if (r == null) {
                throw ResponseException(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: Serve() returned a null response.")
            } else {
                r.requestMethod = method
                r.send(outputStream)
            }
        } catch (ioe: IOException) {
            Response(Status.INTERNAL_ERROR, MimeType.MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: ${ioe.message}").run { send(outputStream) }
            safeClose(outputStream)
        } catch (re: ResponseException) {
            Response(re.status, MimeType.MIME_PLAINTEXT, re.message).run { send(outputStream) }
            safeClose(outputStream)
        } finally {
            tempFileManager.clear()
        }
    }

    fun parseBody(files: MutableMap<String, String>) {
        var randomAccessFile: RandomAccessFile? = null
        var `in`: BufferedReader? = null
        try {
            randomAccessFile = tmpBucket
            var size: Long =
                if (headers.containsKey("content-length")) {
                    headers["content-length"]?.toInt()?.toLong() ?: 0
                } else if (splitbyte < rlen) {
                    (rlen - splitbyte).toLong()
                } else {
                    0
                }

            // Now read all the body and write it to f
            val buf = ByteArray(512)
            while (rlen >= 0 && size > 0) {
                rlen = inputStream.read(buf, 0, 512)
                size -= rlen.toLong()
                if (rlen > 0) {
                    randomAccessFile!!.write(buf, 0, rlen)
                }
            }

            // Get the raw body as a byte []
            val fbuf: ByteBuffer = randomAccessFile!!.channel.map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length())
            randomAccessFile.seek(0)

            // Create a BufferedReader for easily reading it as string.
            val bin: InputStream = FileInputStream(randomAccessFile.fd)
            `in` = BufferedReader(InputStreamReader(bin))

            // If the method is POST, there may be parameters
            // in data section, too, read it:
            if (Method.POST == method) {
                var contentType = ""
                val contentTypeHeader = headers["content-type"]
                var st: StringTokenizer? = null
                if (contentTypeHeader != null) {
                    st = StringTokenizer(contentTypeHeader, ",; ")
                    if (st.hasMoreTokens())
                        contentType = st.nextToken()
                }
                if ("multipart/form-data".equals(contentType, ignoreCase = true)) {
                    // Handle multipart/form-data
                    if (!st!!.hasMoreTokens())
                        throw ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html")
                    val boundaryStartString = "boundary="
                    val boundaryContentStart = contentTypeHeader!!.indexOf(boundaryStartString) + boundaryStartString.length
                    var boundary = contentTypeHeader.substring(boundaryContentStart, contentTypeHeader.length)
                    if (boundary.startsWith("\"") && boundary.startsWith("\""))
                        boundary = boundary.substring(1, boundary.length - 1)
                    decodeMultipartData(boundary, fbuf, `in`, parms, files)
                } else {
                    // Handle application/x-www-form-urlencoded
                    var postLine = ""
                    val pbuf = CharArray(512)
                    var read = `in`.read(pbuf)
                    while (read >= 0 && !postLine.endsWith("\r\n")) {
                        postLine += String(pbuf, 0, read)
                        read = `in`.read(pbuf)
                    }
                    postLine = postLine.trim { it <= ' ' }
                    decodeParms(postLine, parms)
                }
            } else if (Method.PUT == method) {
                files["content"] = saveTmpFile(fbuf, 0, fbuf.limit())
            }
        } finally {
            safeClose(randomAccessFile)
            safeClose(`in`)
        }
    }

    /**
     * Decodes the sent headers and loads the data into Key/value pairs
     */
    private fun decodeHeader(
        `in`: BufferedReader,
        pre: MutableMap<String, String>,
        parms: MutableMap<String, String>,
        header: MutableMap<String, String>
    ) {
        try {
            // Read the request line
            val inLine = `in`.readLine() ?: return
            val st = StringTokenizer(inLine)
            if (!st.hasMoreTokens())
                throw ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html")

            pre["method"] = st.nextToken()
            if (!st.hasMoreTokens())
                throw ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html")

            var uri = st.nextToken()

            // Decode parameters from the URI
            val qmi = uri.indexOf('?')
            uri = if (qmi >= 0) {
                decodeParms(uri.substring(qmi + 1), parms)
                decodePercent(uri.substring(0, qmi))
            } else {
                decodePercent(uri)
            }

            // If there's another token, it's protocol version,
            // followed by HTTP headers. Ignore version but parse headers.
            // NOTE: this now forces header names lowercase since they are
            // case insensitive and vary by client.
            if (st.hasMoreTokens()) {
                var line = `in`.readLine()
                while (line != null && line.trim { it <= ' ' }.isNotEmpty()) {
                    val p = line.indexOf(':')
                    if (p >= 0) header[line.substring(0, p).trim { it <= ' ' }
                        .lowercase(Locale.getDefault())] = line.substring(p + 1).trim { it <= ' ' }
                    line = `in`.readLine()
                }
            }
            pre["uri"] = uri
        } catch (ioe: IOException) {
            throw ResponseException(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.message)
        }
    }

    /**
     * Decodes the Multipart Body data and put it into Key/Value pairs.
     */
    private fun decodeMultipartData(
        boundary: String,
        fbuf: ByteBuffer,
        `in`: BufferedReader,
        parms: MutableMap<String, String>,
        files: MutableMap<String, String>
    ) {
        try {
            val bpositions = getBoundaryPositions(fbuf, boundary.toByteArray())
            var boundarycount = 1
            var mpline = `in`.readLine()
            while (mpline != null) {
                if (!mpline.contains(boundary))
                    throw ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but next chunk does not start with boundary. Usage: GET /example/file.html")
                boundarycount++
                val item: MutableMap<String, String?> = HashMap()
                mpline = `in`.readLine()
                while (mpline != null && mpline.trim { it <= ' ' }.isNotEmpty()) {
                    val p = mpline.indexOf(':')
                    if (p != -1) {
                        item[mpline.substring(0, p).trim { it <= ' ' }
                            .lowercase(Locale.getDefault())] = mpline.substring(p + 1).trim { it <= ' ' }
                    }
                    mpline = `in`.readLine()
                }
                if (mpline != null) {
                    val contentDisposition = item["content-disposition"] ?: throw ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but no content-disposition info found. Usage: GET /example/file.html")
                    val st = StringTokenizer(contentDisposition, "; ")
                    val disposition: MutableMap<String, String> = HashMap()
                    while (st.hasMoreTokens()) {
                        val token = st.nextToken()
                        val p = token.indexOf('=')
                        if (p != -1) {
                            disposition[token.substring(0, p).trim { it <= ' ' }
                                .lowercase(Locale.getDefault())] = token.substring(p + 1).trim { it <= ' ' }
                        }
                    }
                    var pname = disposition["name"]
                    pname = pname!!.substring(1, pname.length - 1)
                    var value = ""
                    if (item["content-type"] == null) {
                        while (mpline != null && !mpline.contains(boundary)) {
                            mpline = `in`.readLine()
                            if (mpline != null) {
                                val d = mpline.indexOf(boundary)
                                value += if (d == -1) mpline else mpline.substring(0, d - 2)
                            }
                        }
                    } else {
                        if (boundarycount > bpositions.size)
                            throw ResponseException(Status.INTERNAL_ERROR, "Error processing request")
                        val offset = stripMultipartHeaders(fbuf, bpositions[boundarycount - 2])
                        val path = saveTmpFile(fbuf, offset, bpositions[boundarycount - 1] - offset - 4)
                        files[pname] = path
                        value = disposition["filename"] ?: ""
                        value = value.substring(1, value.length - 1)
                        do {
                            mpline = `in`.readLine()
                        } while (mpline != null && !mpline.contains(boundary))
                    }
                    parms[pname] = value
                }
            }
        } catch (ioe: IOException) {
            throw ResponseException(Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: ${ioe.message}")
        }
    }

    /**
     * Retrieves the content of a sent file and saves it to a temporary file. The full path to the saved file is returned.
     */
    private fun saveTmpFile(b: ByteBuffer, offset: Int, len: Int): String {
        var path = ""
        if (len > 0) {
            var fileOutputStream: FileOutputStream? = null
            try {
                val tempFile = tempFileManager.createTempFile()
                val src = b.duplicate()
                fileOutputStream = FileOutputStream(tempFile.name)
                val dest = fileOutputStream.channel
                src.position(offset).limit(offset + len)
                dest.write(src.slice())
                path = tempFile.name
            } catch (e: Exception) { // Catch exception if any
                Log.d("HTTPSession", "Error: ${e.message}")
            } finally {
                safeClose(fileOutputStream)
            }
        }
        return path
    }
}

/**
 * Find byte index separating header from body. It must be the last byte of the first two sequential new lines.
 */
private fun findHeaderEnd(buf: ByteArray, rlen: Int): Int {
    var splitbyte = 0
    while (splitbyte + 3 < rlen) {
        if (buf[splitbyte] == '\r'.code.toByte() && buf[splitbyte + 1] == '\n'.code.toByte() &&
            buf[splitbyte + 2] == '\r'.code.toByte() && buf[splitbyte + 3] == '\n'.code.toByte()) {
            return splitbyte + 4
        }
        splitbyte++
    }
    return 0
}

/**
 * Find the byte positions where multipart boundaries start.
 */
fun getBoundaryPositions(b: ByteBuffer, boundary: ByteArray): IntArray {
    var matchcount = 0
    var matchbyte = -1
    val matchbytes: MutableList<Int> = ArrayList()
    run {
        var i = 0
        while (i < b.limit()) {
            if (b[i] == boundary[matchcount]) {
                if (matchcount == 0) matchbyte = i
                matchcount++
                if (matchcount == boundary.size) {
                    matchbytes.add(matchbyte)
                    matchcount = 0
                    matchbyte = -1
                }
            } else {
                i -= matchcount
                matchcount = 0
                matchbyte = -1
            }
            i++
        }
    }
    val ret = IntArray(matchbytes.size)
    for (i in ret.indices) {
        ret[i] = matchbytes[i]
    }
    return ret
}

/**
 * It returns the offset separating multipart file headers from the file's data.
 */
private fun stripMultipartHeaders(b: ByteBuffer, offset: Int): Int {
    var i: Int = offset
    while (i < b.limit()) {
        if (b[i] == '\r'.code.toByte() && b[++i] == '\n'.code.toByte() &&
            b[++i] == '\r'.code.toByte() && b[++i] == '\n'.code.toByte()) break
        i++
    }
    return i + 1
}

/**
 * Decodes parameters in percent-encoded URI-format ( e.g. "name=Jack%20Daniels&pass=Single%20Malt" ) and
 * adds them to given Map. NOTE: this doesn't support multiple identical keys due to the simplicity of Map.
 */
private fun decodeParms(parms: String?, p: MutableMap<String, String>) {
    if (parms == null) {
        p[QUERY_STRING_PARAMETER] = ""
        return
    }
    p[QUERY_STRING_PARAMETER] = parms
    val st = StringTokenizer(parms, "&")
    while (st.hasMoreTokens()) {
        val e = st.nextToken()
        val sep = e.indexOf('=')
        if (sep >= 0) {
            p[decodePercent(e.substring(0, sep)).trim { it <= ' ' }] = decodePercent(e.substring(sep + 1))
        } else {
            p[decodePercent(e).trim { it <= ' ' }] = ""
        }
    }
}