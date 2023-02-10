package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class Role(val id: Int, val name: String)


class RoleService(private val connection: Connection) : Service {

    companion object {
        private const val CREATE_TABLE_ROLES =
            "CREATE TABLE IF NOT EXISTS roles (id SERIAL PRIMARY KEY, name TEXT NOT NULL);"
        private const val SELECT_ROLES = "SELECT * FROM roles"
        private const val SELECT_ROLE_BY_ID = "SELECT * FROM roles WHERE id=?;"
        private const val INSERT_ROLES = "INSERT INTO roles (name) VALUES (?);"
        private const val UPDATE_ROLES = "UPDATE roles SET name = ? WHERE id = ?;"

    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_ROLES)
    }

     override suspend fun getAll() : List<Role> = withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SELECT_ROLES)

        val resultSet = statement.executeQuery()
        val rolesList = mutableListOf<Role>()
        while(resultSet.next()) {
            val name = resultSet.getString("name")
            val id = resultSet.getInt("id")
            rolesList.add(Role(id, name))
        }
        return@withContext rolesList
    }

     override suspend fun getById(id: Int): Role =  withContext(Dispatchers.IO){
        val statement = connection.prepareStatement(SELECT_ROLE_BY_ID)
        statement.setInt(1, id)

        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val name = resultSet.getString("name")

            return@withContext Role(id, name)
        } else {
            throw Exception("No roles found by id=$id")
        }
    }

     override suspend fun create(obj: Any): Int = withContext(Dispatchers.IO) {
         if (obj is Role) {
             val statement = connection.prepareStatement(INSERT_ROLES)
             statement.setString(1, obj.name)
             statement.executeQuery()
             val resultSet = statement.resultSet
             if(resultSet.next()) {
                 return@withContext resultSet.getInt(1)
             } else {
                 throw Exception("error in creating employee")
             }
         } else {
             throw Exception("error in create role")
         }
    }

     override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
         if (obj is Role) {
             val statement = connection.prepareStatement(UPDATE_ROLES)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.id)
             statement.executeUpdate()
         }else {
             throw Exception("error in update role")
         }
     }

    override suspend fun delete(id: Int) = throw Exception("Not Allowed")
}