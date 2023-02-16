package com.example.model

import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

/**
 * Класс классификации.
 * @property id - уникальный идентификатор классификации.
 * @property name - название / имя классификации.
 */
@Serializable
data class Classification(val id: Int, val name: String)

/**
 * Класс для создания интерфейса между API БД.
 * @property connection - соединенние с БД.
 * Реализует интерфейс @see Service.
 */
class ClassificationService(private val connection: Connection): Service {

    /**
     * Статические переменные класса.
     */
    companion object {
        private const val CREATE_TABLE_CLASSIFICATION =
            "CREATE TABLE IF NOT EXISTS classification (id SERIAL PRIMARY KEY, name TEXT NOT NULL);"
        private const val SELECT_CLASSIFICATION = "SELECT * FROM classification"
        private const val SELECT_CLASSIFICATION_BY_ID = "SELECT * FROM classification WHERE id=?;"
        private const val INSERT_CLASSIFICATION = "INSERT INTO classification (name) VALUES (?);"
        private const val UPDATE_CLASSIFICATION = "UPDATE classification SET name = ? WHERE id = ?;"
    }

    /**
     * Метод, срабатывающий при инициализации объекта.
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_CLASSIFICATION)
    }

    /**
     * Метод получения объекта из БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     * @return возвращает объект типа @see Classification.
     * @throws Exception("no classifications with $id found")
     */
    override suspend fun getById(id: Int): Classification = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_CLASSIFICATION_BY_ID)
        statement.setInt(1,id)

        val resultSet = statement.executeQuery()
        if(resultSet.next()) {
            val name = resultSet.getString("name")
            return@withContext Classification(id, name)
        } else {
            throw Exception("no classifications with $id found")
        }
    }

    /**
     * Метод получения всех объектов из БД.
     * @return возвращает список типа @see Classification.
     */
    override suspend fun getAll(): List<Classification> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_CLASSIFICATION)

        val resultSet = statement.executeQuery()
        val classList = mutableListOf<Classification>()
        while(resultSet.next()) {
            val name = resultSet.getString("name")
            val id = resultSet.getInt("id")
            classList.add(Classification(id, name))
        }
        return@withContext classList
    }

    /**
     * Метод создания нового объекта в БД.
     * @param obj - объект типа @see Classification.
     * @return возвращает уникальный идентификатор созданного объекта.
     * @throws Exception("error in creating classification")
     */
    override suspend fun create(obj: Any): Int = withContext(Dispatchers.IO) {
        if (obj is Classification) {
        val statement = connection.prepareStatement(INSERT_CLASSIFICATION)
        statement.setString(1, obj.name)
        statement.executeQuery()
        val resultSet = statement.resultSet
        if(resultSet.next()) {
            return@withContext resultSet.getInt(1)
        } else {
            throw Exception("error in creating classification")
        }
    } else {
        throw Exception("error in create classification")
    }
    }


    /**
     * Метод обновления данного объекта в БД.
     * @param obj - объект типа @see Classification.
     * @throws Exception("error in updating classification")
     */
    override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
        if (obj is Classification) {
            val statement = connection.prepareStatement(UPDATE_CLASSIFICATION)
            statement.setString(1, obj.name)
            statement.setInt(2, obj.id)
            statement.executeUpdate()
        } else {
            throw Exception("error in updating classification")
        }
    }


    /**
     * Метод удаления объекта в БД по уникальному идентификатору.
     * @throws Exception("Not Allowed")
     */
    override suspend fun delete(id: Int) = throw Exception("Not Allowed")
}
