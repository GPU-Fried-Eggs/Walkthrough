package com.kotlin.walkthrough.artifacts.todo.server.tempfiles

import com.kotlin.walkthrough.artifacts.todo.server.safeClose
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DefaultTempFile(tempdir: File) : TempFile {
    private val file: File
    private val fstream: OutputStream

    override val name: String
        get() = file.absolutePath

    init {
        file = File.createTempFile("Walkthrough-", "", tempdir)
        fstream = FileOutputStream(file)
    }

    override fun open(): OutputStream {
        return fstream
    }

    override fun delete() {
        safeClose(fstream)
        file.delete()
    }
}
