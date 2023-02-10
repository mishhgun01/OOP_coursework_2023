package com.example.model

import java.sql.Connection

abstract class AbstractService(connection: Connection)  {
    abstract suspend fun getById(id: Int): Any
    abstract suspend fun getAll(): List<Any>
    abstract suspend fun create(obj: Any): Int
    abstract suspend fun update(obj: Any)
    abstract suspend fun delete(id: Int)
}