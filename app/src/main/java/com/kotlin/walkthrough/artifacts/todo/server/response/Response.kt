package com.kotlin.walkthrough.artifacts.todo.server.response

import android.util.Log
import com.kotlin.walkthrough.artifacts.todo.server.MimeType
import com.kotlin.walkthrough.artifacts.todo.server.request.Method
import com.kotlin.walkthrough.artifacts.todo.server.safeClose
import java.io.*
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

const val BUFFER_SIZE = 16 * 1024

class Response(var status: Status, var mimeType: MimeType, var data: InputStream) : Closeable {
    private var header: HashMap<String, String> = HashMap()
    var requestMethod: Method? = null

    constructor(status: Status, mimeType: MimeType, txt: String) : this(
        status = status,
        mimeType = mimeType,
        data = ByteArrayInputStream(txt.toByteArray(StandardCharsets.UTF_8))
    )

    override fun close() {
        data.close()
    }

    fun addHeader(name: String, value: String) {
        header[name] = value
    }

    fun send(outputStream: OutputStream) {
        val gmtFrmt = SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US)
        var pending = data.available() ?: -1

        try {
            PrintWriter(outputStream).run {
                print("HTTP/1.1 ${status}\r\n")
                print("Content-Type: $mimeType\r\n")
                if (header["Date"] == null) print("Date: ${gmtFrmt.format(Date())}\r\n")
                for (key in header.keys) print("$key: ${header[key]}\r\n")
                if (pending > 0) {
                    print("Connection: keep-alive\r\n")
                    print("Content-Length: $pending\r\n")
                }
                print("\r\n")
                flush()
            }

            if (requestMethod != Method.HEAD) {
                val buff = ByteArray(BUFFER_SIZE)
                while (pending > 0) {
                    val read = data.read(buff, 0, min(pending, BUFFER_SIZE))
                    if (read <= 0) break
                    outputStream.write(buff, 0, read)
                    pending -= read
                }
            }
            outputStream.flush()
            safeClose(data)
        } catch (ioe: IOException) {
            Log.d("Response", "Could not send response to the client", ioe)
        }
    }
}
