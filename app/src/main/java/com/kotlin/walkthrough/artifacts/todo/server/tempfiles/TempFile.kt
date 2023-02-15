package com.kotlin.walkthrough.artifacts.todo.server.tempfiles

import java.io.OutputStream

interface TempFile {
    fun open(): OutputStream

    fun delete()

    val name: String
}
