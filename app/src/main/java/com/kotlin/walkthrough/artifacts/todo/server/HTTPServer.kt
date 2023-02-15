package com.kotlin.walkthrough.artifacts.todo.server

import android.util.Log
import com.kotlin.walkthrough.artifacts.todo.server.request.Method
import com.kotlin.walkthrough.artifacts.todo.server.response.Response
import com.kotlin.walkthrough.artifacts.todo.server.response.Status
import com.kotlin.walkthrough.artifacts.todo.server.tempfiles.DefaultTempFileManagerFactory
import com.kotlin.walkthrough.artifacts.todo.server.tempfiles.TempFileManagerFactory
import com.kotlin.walkthrough.artifacts.todo.server.threading.AsyncRunner
import com.kotlin.walkthrough.artifacts.todo.server.threading.DefaultAsyncRunner
import java.io.Closeable
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.*

const val SOCKET_READ_TIMEOUT = 5000
const val QUERY_STRING_PARAMETER = "QUERY_STRING_KEY"

abstract class HTTPServer(val hostname: String?, val port: Int) {
    private var thread: Thread? = null
    private var serverSocket: ServerSocket = ServerSocket()
    private val asyncRunner: AsyncRunner = DefaultAsyncRunner()
    private val tempFileManagerFactory: TempFileManagerFactory = DefaultTempFileManagerFactory()

    init {
        serverSocket.reuseAddress = true
        serverSocket.bind(if (hostname != null) InetSocketAddress(hostname, port) else InetSocketAddress(port)) // if error, application must exist avoid unknown behaviour
    }

    constructor(port: Int) : this(null, port)

    fun start(timeout: Int = SOCKET_READ_TIMEOUT, daemon: Boolean = true) {
        thread = Thread {
            do {
                try {
                    val finalAccept: Socket = serverSocket.accept()
                    if (timeout > 0) finalAccept.soTimeout = timeout
                    val inputStream = finalAccept.getInputStream()
                    if (inputStream == null) {
                        safeClose(finalAccept)
                    } else {
                        asyncRunner.exec {
                            val outputStream = finalAccept.getOutputStream()
                            try {
                                val tempFileManager = tempFileManagerFactory.create()
                                val session = HTTPSession(this, tempFileManager, inputStream, outputStream)
                                while (!finalAccept.isClosed) {
                                    session.execute()
                                }
                            } catch (ioe: IOException) {
                                ioe.printStackTrace()
                            } finally {
                                safeClose(outputStream)
                                safeClose(inputStream)
                                safeClose(finalAccept)
                            }
                        }
                    }
                } catch (ioe: IOException) {
                    Log.d("HTTPServer", "Communication with the client broken", ioe)
                    Thread.sleep(10L)
                }
            } while (!serverSocket.isClosed)
        }
        thread?.isDaemon = daemon
        thread?.name = "Main Listener"
        thread?.start()
    }

    fun stop() {
        try {
            safeClose(serverSocket)
            thread?.join()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun serve(
        uri: String?,
        method: Method?,
        header: Map<String, String>,
        parms: Map<String, String>,
        files: Map<String, String>
    ): Response?

    fun serve(session: HTTPSession) : Response? {
        val files: MutableMap<String, String> = HashMap()

        try {
            session.parseBody(files)
        } catch (ioe: IOException) {
            return Response(
                Status.INTERNAL_ERROR,
                MimeType.MIME_PLAINTEXT,
                "SERVER INTERNAL ERROR: IOException: " + ioe.message
            )
        } catch (re: ResponseException) {
            return Response(re.status, MimeType.MIME_PLAINTEXT, re.message)
        }

        val uri: String? = session.uri
        val method: Method? = session.method
        val parms: Map<String, String> = session.parms
        val headers: Map<String, String> = session.headers
        return serve(uri, method, headers, parms, files)
    }
}

class ResponseException(val status: Status, override val message: String = "", exception: Exception? = null) : Exception(message, exception)

fun safeClose(closeable: Any?) {
    try {
        when {
            closeable != null -> {
                when (closeable) {
                    is Closeable -> {
                        closeable.close()
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown object to close")
                    }
                }
            }
        }
    } catch (e: IOException) {
        Log.d("HTTPServer", "Could not close", e)
    }
}

/**
 * Decode percent encoded `String` values.
 * @param str the percent encoded `String`
 * @return expanded form of the input, for example "foo%20bar" becomes "foo bar"
 */
fun decodePercent(str: String): String {
    return StringBuilder().let {
        var i = 0
        while (i < str.length) {
            when (val c = str[i]) {
                '+' -> it.append(' ')
                '%' -> {
                    it.append(str.substring(i + 1, i + 3).toInt(16).toChar())
                    i += 2
                }
                else -> it.append(c)
            }
            i++
        }
        it.toString()
    }
}

fun decodeParameters(parms: Map<String, String?>) : Map<String, MutableList<String>> {
    return decodeParameters(parms[QUERY_STRING_PARAMETER])
}

fun decodeParameters(queryString: String?): Map<String, MutableList<String>> {
    val parms: MutableMap<String, MutableList<String>> = HashMap()
    if (queryString != null) {
        val st = StringTokenizer(queryString, "&")
        while (st.hasMoreTokens()) {
            val e = st.nextToken()
            val sep = e.indexOf('=')
            val propertyName = decodePercent(if (sep >= 0) e.substring(0, sep) else e).trim { it <= ' ' }
            if (!parms.containsKey(propertyName))
                parms[propertyName] = ArrayList()
            val propertyValue = if (sep >= 0) decodePercent(e.substring(sep + 1)) else null
            if (propertyValue != null)
                parms[propertyName]!!.add(propertyValue)
        }
    }
    return parms
}