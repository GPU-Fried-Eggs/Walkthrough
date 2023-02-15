package com.kotlin.walkthrough.artifacts.todo.server

enum class MimeType(val type: String) {
    MIME_PLAINTEXT("text/plain"),
    MIME_HTML("text/html"),
    MIME_XML("text/xml"),
    MIME_JSON("application/json")
}