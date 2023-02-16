package com.example.model

import com.example.plugins.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

/**
 * Класс роли.
 * @property id - уникальный идентификатор роли.
 * @property name - название / имя роли.
 */
@Serializable
data class Role(val id: Int, val name: String, val classification: Classification)

/**
 * Класс для создания интерфейса между API БД.
 * @property connection - соединенние с БД.
 * Реализует интерфейс @see Service.
 */
class RoleService(private val connection: Connection) : Service {

    /**
     * Статические переменные класса.
     */
    companion object {
        private const val CREATE_TABLE_ROLES =
            "CREATE TABLE IF NOT EXISTS roles (id SERIAL PRIMARY KEY, name TEXT NOT NULL, classification_id INTEGER REFERENCES classification(id));"
        private const val SELECT_ROLES = "SELECT * FROM roles"
        private const val SELECT_ROLE_BY_ID = "SELECT * FROM roles WHERE id=?;"
        private const val INSERT_ROLES = "INSERT INTO roles (name, classification_id) VALUES (?, ?);"
        private const val UPDATE_ROLES = "UPDATE roles SET name = ?, classification_id=? WHERE id = ?;"

    }

    /**
     * Метод, срабатывающий при инициализации объекта.
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_ROLES)
    }

    /**
     * Метод получения всех ролей из БД.
     * @return возвращает список объектов типа @see Role.
     */
     override suspend fun getAll() : List<Role> = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SELECT_ROLES)

        val resultSet = statement.executeQuery()
        val rolesList = mutableListOf<Role>()
        val classes = ClassificationService(connection).getAll()
        while(resultSet.next()) {
            val name = resultSet.getString("name")
            val id = resultSet.getInt("id")
            val classification_id = resultSet.getInt("classification_id")
            val classification = classes.find { it.id==classification_id }
            classification?.let { Role(id, name, it) }?.let { rolesList.add(it) }
        }
        return@withContext rolesList
    }

    /**
     * Метод получения объекта из БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     * @return возвращает объект типа @see Role.
     * @throws Exception("No roles with id=$id found").
     */
     override suspend fun getById(id: Int): Role =  withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SELECT_ROLE_BY_ID)
        statement.setInt(1, id)

        val resultSet = statement.executeQuery()
         if (resultSet.next()) {
            val name = resultSet.getString("name")
             val classification_id = resultSet.getInt("classification_id")
             val classes = ClassificationService(connection).getAll()
             val classification = classes.find { it.id==classification_id }
             classification?.let {
                 return@withContext Role(id, name, it)
             }
             throw Exception("No roles with id=$id found")
        } else {
            throw Exception("No roles with id=$id found")
        }
    }

    /**
     * Метод создания нового объекта в БД.
     * @param obj - объект типа @see Role
     * @return возвращает уникальный идентификатор созданного объекта.
     * @throws Exception("error in creating role")
     */
     override suspend fun create(obj: Any): Int = withContext(Dispatchers.IO) {
         if (obj is Role) {
             val statement = connection.prepareStatement(INSERT_ROLES)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.classification.id)
             statement.executeQuery()
             val resultSet = statement.resultSet
             if(resultSet.next()) {
                 return@withContext resultSet.getInt(1)
             } else {
                 throw Exception("error in creating role")
             }
         } else {
             throw Exception("error in creating role")
         }
    }

    /**
     * Метод обновления данного объекта в БД.
     * @param obj - объект типа @see Role.
     * @throws Exception("error in updating role")
     */
     override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
         if (obj is Role) {
             val statement = connection.prepareStatement(UPDATE_ROLES)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.id)
             statement.setInt(3, obj.classification.id)
             statement.executeUpdate()
         }else {
             throw Exception("error in update role")
         }
     }

    /**
     * Метод обновления данного объекта в БД.
     * @throws Exception("Not Allowed")
     */
    override suspend fun delete(id: Int) = throw Exception("Not Allowed")
}