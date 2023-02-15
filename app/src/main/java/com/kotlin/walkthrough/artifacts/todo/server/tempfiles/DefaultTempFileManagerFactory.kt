package com.kotlin.walkthrough.artifacts.todo.server.tempfiles

class DefaultTempFileManagerFactory : TempFileManagerFactory {
    override fun create(): TempFileManager {
        return DefaultTempFileManager()
    }
}