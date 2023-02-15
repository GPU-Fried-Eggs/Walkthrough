package com.kotlin.walkthrough.artifacts.todo.server.tempfiles

interface TempFileManagerFactory {
    fun create(): TempFileManager
}
