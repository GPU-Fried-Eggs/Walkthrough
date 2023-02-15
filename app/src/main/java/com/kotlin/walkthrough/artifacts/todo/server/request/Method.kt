package com.kotlin.walkthrough.artifacts.todo.server.request

enum class Method {
    GET, PUT, POST, DELETE, HEAD;

    companion object {
        fun lookup(method: String?): Method? {
            for (m in Method.values()) {
                if (m.toString().equals(method, ignoreCase = true)) {
                    return m
                }
            }
            return null
        }
    }
}