package com.kotlin.walkthrough.artifacts.todo.server.threading

interface AsyncRunner {
    fun exec(code: Runnable?)
}
