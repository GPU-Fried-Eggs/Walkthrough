package com.kotlin.walkthrough.artifacts.todo.server.tempfiles

/**
 * Temp file manager.
 *
 * Temp file managers are created 1-to-1 with incoming requests, to create and cleanup
 * temporary files created as a result of handling the request.
 */
interface TempFileManager {
    fun createTempFile(): TempFile

    fun clear()
}