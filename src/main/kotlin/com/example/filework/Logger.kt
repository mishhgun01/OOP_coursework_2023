package com.example.filework

import io.ktor.utils.io.errors.*
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.TimeMark

class Logger(private val path: String): FileWorker {
    override fun upload(info: Any) {
        val dateTime = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val text = "${dateTime.format(Date())}: $info\n"
        val path = Paths.get(path)
        try {
            Files.write(
                path,
                text.toByteArray(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getFromFile(path: String): Any {
        TODO("Not yet implemented")
    }
}