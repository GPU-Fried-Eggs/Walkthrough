package com.kotlin.walkthrough.artifacts.todo

import com.kotlin.walkthrough.artifacts.todo.server.HTTPServer
import com.kotlin.walkthrough.artifacts.todo.server.MimeType
import com.kotlin.walkthrough.artifacts.todo.server.request.Method
import com.kotlin.walkthrough.artifacts.todo.server.response.Response
import com.kotlin.walkthrough.artifacts.todo.server.response.Status
import java.util.concurrent.ConcurrentHashMap

class TodoServer : HTTPServer(8686) {
    private var data: ConcurrentHashMap<Int, String> = ConcurrentHashMap()

    init {
        data[1] = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        data[2] = "tellus molestie nunc non blandit, massa enim nec dui nunc mattis enim ut tellus elementum sagittis vitae et leo duis."
        data[3] = "viverra accumsan in nisl nisi scelerisque eu ultrices, vitae auctor eu augue ut lectus arcu bibendum at varius vel pharetra."
    }

    override fun serve(
        uri: String?,
        method: Method?,
        header: Map<String, String>,
        parms: Map<String, String>,
        files: Map<String, String>
    ): Response {
        return try {
            when (method) {
                Method.GET -> {
                    when (uri) {
                        "/todos" -> {
                            val id = parms["id"]?.toInt()
                            if (id != null)
                                Response(Status.OK, MimeType.MIME_JSON, wrapper(id))
                            else
                                Response(Status.OK, MimeType.MIME_JSON, "[${data.map { wrapper(it.key) }.joinToString()}]")
                        }
                        else -> {
                            Response(Status.NOT_FOUND, MimeType.MIME_PLAINTEXT, "Invalid get request")
                        }
                    }
                }
                Method.POST -> {
                    when (uri) {
                        "/todos" -> {
                            data[data.size + 1] = files.values.toString()
                            Response(Status.OK, MimeType.MIME_PLAINTEXT, "true")
                        }
                        else -> {
                            Response(Status.NOT_FOUND, MimeType.MIME_PLAINTEXT, "Invalid post request")
                        }
                    }
                }
                else -> {
                    Response(Status.NOT_FOUND, MimeType.MIME_PLAINTEXT, "Invalid method")
                }
            }
        } catch (err: Exception) {
            Response(Status.INTERNAL_ERROR, MimeType.MIME_PLAINTEXT, "Unknown request!")
        }
    }

    private fun wrapper(id: Int) : String {
        return "{\"id\":$id,\"content\":\"${data[id]}\"}"
    }

    companion object {
        val instance: TodoServer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TodoServer()
        }
    }
}
