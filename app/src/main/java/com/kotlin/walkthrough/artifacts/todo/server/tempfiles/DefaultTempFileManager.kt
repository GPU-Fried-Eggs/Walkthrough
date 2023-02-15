package com.kotlin.walkthrough.artifacts.todo.server.tempfiles

import android.util.Log
import java.io.File

class DefaultTempFileManager : TempFileManager {
    private val tmpdir: File = File(System.getProperty("java.io.tmpdir") as String)
    private val tempFiles: MutableList<TempFile> = ArrayList()

    override fun createTempFile(): TempFile {
        val tempFile = DefaultTempFile(tmpdir)
        tempFiles.add(tempFile)
        return tempFile
    }

    override fun clear() {
        for (file in tempFiles) {
            try {
                file.delete()
            } catch (e: Exception) {
                Log.d("DefaultTempFileManager", "could not delete file ", e)
            }
        }
        tempFiles.clear()
    }
}
