package com.kotlin.walkthrough.artifacts.todo.server.threading

class DefaultAsyncRunner : AsyncRunner {
    private var requestCount: Long = 0

    override fun exec(code: Runnable?) {
        ++requestCount
        val t = Thread(code)
        t.isDaemon = true
        t.name = "Http Request Processor (#$requestCount)"
        t.start()
    }
}
