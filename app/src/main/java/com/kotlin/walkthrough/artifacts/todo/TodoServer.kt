package com.kotlin.walkthrough.artifacts.todo

import com.kotlin.walkthrough.artifacts.todo.server.HTTPServer
import com.kotlin.walkthrough.artifacts.todo.server.MimeType
import com.kotlin.walkthrough.artifacts.todo.server.request.Method
import com.kotlin.walkthrough.artifacts.todo.server.response.Response
import com.kotlin.walkthrough.artifacts.todo.server.response.Status
import java.util.concurrent.ConcurrentHashMap

class TodoServer : HTTPServer(8088) {
    private var data: ConcurrentHashMap<Int, String> = ConcurrentHashMap()

    override fun serve(
        uri: String?,
        method: Method?,
        header: Map<String, String>,
        parms: Map<String, String>,
        files: Map<String, String>
    ): Response {
        return try {
            when (uri) {
                "/all" -> {
                    Response(Status.OK, MimeType.MIME_JSON, "{${data.map { wrapper(it.key) }.joinToString()}}")
                }
                "/get" -> {
                    val id = parms["id"]?.toInt()
                    if (id != null)
                        Response(Status.OK, MimeType.MIME_JSON, wrapper(id))
                    else
                        Response(Status.NOT_FOUND, MimeType.MIME_PLAINTEXT, "Invalid id")
                }
                else -> {
                    Response(Status.NOT_FOUND, MimeType.MIME_PLAINTEXT, "Invalid request")
                }
            }
        } catch (err: Exception) {
            Response(Status.INTERNAL_ERROR, MimeType.MIME_PLAINTEXT, "Unknown request!")
        }
    }

    private fun wrapper(id: Int) : String {
        return "{id:$id,content:${data[id]}}"
    }
}
