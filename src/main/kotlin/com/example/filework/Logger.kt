package com.example.filework

import java.io.File
import java.io.FileWriter

class Logger(val path: String): FileWorker {
    override fun upload(info: Any) {
        val file = File(path)
        val text = info.toString()

        FileWriter(file).use { it.write(text) }
    }

    override fun getFromFile(path: String): Any {
        TODO("Not yet implemented")
    }
}