package com.example.model

interface Service  {
    suspend fun getById(id: Int): Any
    suspend fun getAll(): List<Any>
    suspend fun create(obj: Any): Int
    suspend fun update(obj: Any)
    suspend fun delete(id: Int)
}