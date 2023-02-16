package com.example.model

/**
 * Интерфейс для создания интерфейса между API БД.
 */
interface Service  {
    /**
     * Метод получения объекта из БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     * @return возвращает объект типа Any.
     */
    suspend fun getById(id: Int): Any

    /**
     * Метод получения всех объектов из БД.
     * @return возвращает список объектов типа Any.
     */
    suspend fun getAll(): List<Any>

    /**
     * Метод создания нового объекта в БД.
     * @param obj - объект типа Any.
     * @return возвращает уникальный идентификатор созданного объекта.
     */
    suspend fun create(obj: Any): Int

    /**
     * Метод обновления данного объекта в БД.
     * @param obj - объект типа @see Any.
     */
    suspend fun update(obj: Any)

    /**
     * Метод удаления объекта в БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     */
    suspend fun delete(id: Int)
}