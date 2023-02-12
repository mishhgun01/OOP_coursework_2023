package com.example.model

import com.example.filework.Logger
import com.example.plugins.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

@Serializable
data class Employee(val id: Int? = null, val name: String, val role: Role, val responsibility: String? = "",
                    val workingDays: List<Int>? = mutableListOf<Int>(), val login: String, val password: String)

class EmployeeService(private val connection: Connection): Service {

    companion object {
        private const val CREATE_TABLE_EMPLOYEES =
            "CREATE TABLE IF NOT EXISTS employees (id SERIAL PRIMARY KEY, fullname TEXT NOT NULL," +
                    "role_id INTEGER REFERENCES roles(id), responsibility TEXT NOT NULL DEFAULT ''," +
                    "working_days integer[] NOT NULL DEFAULT array[]::integer[], login TEXT NOT NULL, password TEXT NOT NULL);"
        private const val SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employees WHERE id = ?"
        private const val SELECT_EMPLOYEE = "SELECT * FROM employees;"
        private const val INSERT_EMPLOYEE = "INSERT INTO employees (fullname, role_id,responsibility, working_days, login, password)" +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id;"
        private const val UPDATE_EMPLOYEE = "UPDATE employees SET fullname = ?, role_id = ?, responsibility = ?, working_days = ?, login = ?, password = ?" +
                " WHERE id = ?"
        private const val DELETE_EMPLOYEE = "DELETE FROM employees WHERE id = ?"

    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_EMPLOYEES)
    }

     override suspend fun getAll(): List<Employee> = withContext(Dispatchers.IO){
         val statement = connection.prepareStatement(SELECT_EMPLOYEE)
         val resultSet = statement.executeQuery()

         val rolesList = RoleService(connection).getAll()
         val eList = mutableListOf<Employee>()
         while(resultSet.next()) {
             val id = resultSet.getInt("id")
             val fullname = resultSet.getString("fullname")
             val roleId = resultSet.getInt("role_id")
             val responsibility = resultSet.getString("responsibility")
             val workingDays = Helper.convertSQLArrayToIntList(resultSet, "working_days")
             val login = resultSet.getString("login")
             val password = resultSet.getString("password")

             val role = rolesList.find { it.id == roleId }
             role?.let {
                 eList.add(Employee(id, fullname, role,responsibility, workingDays, login, password))
             }
         }
         return@withContext eList
    }

     override suspend fun getById(id: Int): Employee = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID)
        statement.setInt(1, id)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {

            val fullname = resultSet.getString("fullName")
            val roleId = resultSet.getInt("role_id")
            val responsibility = resultSet.getString("responsibility")
            val workingDays = Helper.convertSQLArrayToIntList(resultSet, "working_days")
            val login = resultSet.getString("login")
            val password = resultSet.getString("password")

            val rolesList = RoleService(connection).getAll()
            val role = rolesList.find { it.id == roleId }

            role?.let {
                return@withContext Employee(id, fullname, it,responsibility, workingDays, login, password)
            }
            throw Exception("Error in get")
        }
        else {
            throw Exception("Unable to retrieve the id of the newly inserted city")
        }
    }

     override suspend fun create(obj: Any): Int = withContext(Dispatchers.IO) {
         if (obj is Employee) {
             val statement = connection.prepareStatement(INSERT_EMPLOYEE)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.role.id)
             statement.setString(3, obj.responsibility)
             statement.setArray(4, obj.workingDays?.let { Helper.prepareSQLArrayFromIntList(connection, it) })
             statement.setString(5, obj.login)
             statement.setString(6, obj.password)
             statement.executeQuery()
             val resultSet = statement.resultSet
             if(resultSet.next()) {
                 return@withContext resultSet.getInt(1)
             } else {
                 throw Exception("error in creating employee")
             }
         } else {
             throw Exception("error in creating employee")
         }
    }

     override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
         if (obj is Employee) {
             val statement = connection.prepareStatement(UPDATE_EMPLOYEE)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.role.id)
             statement.setString(3, obj.responsibility)
             statement.setArray(4, obj.workingDays?.let { Helper.prepareSQLArrayFromIntList(connection, it) })
             statement.setString(5, obj.login)
             statement.setString(6, obj.password)
             statement.executeUpdate()
         } else {
             throw Exception("error in updating employee")
         }
    }

     override suspend fun delete(id: Int): Unit = withContext(Dispatchers.IO) {
         val statement = connection.prepareStatement(DELETE_EMPLOYEE)
         statement.setInt(1, id)
         statement.executeUpdate()
    }

    suspend fun authEmployee(obj: UserForAuth): Int = withContext(Dispatchers.IO) {
        val statement =connection.prepareStatement("SELECT id, password FROM employees WHERE login=?;")
        statement.setString(1, obj.login)
        statement.executeQuery()
        val resultSet = statement.resultSet
        if (resultSet.next()) {
            val id = resultSet.getInt(1)
            val pwd = resultSet.getString(2)
            if (Helper.compareHashes(obj.password, pwd)) {
                return@withContext id
            } else {
                return@withContext 0
            }
        } else {
            return@withContext 0
        }
    }
}