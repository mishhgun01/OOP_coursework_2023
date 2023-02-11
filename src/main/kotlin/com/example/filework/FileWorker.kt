package com.example.filework

interface FileWorker {
    fun upload(info: Any): Unit
    fun getFromFile(path: String): Any
}