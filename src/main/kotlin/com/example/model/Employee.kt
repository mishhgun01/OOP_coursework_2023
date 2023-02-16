package com.example.model

import com.example.plugins.Helper
import io.ktor.http.cio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Connection

/**
 * Класс сотрудника.
 * @property id - уникальный идентификатоор сотрудника.
 * @property name - полное имя сотрудника.
 * @property role - роль сотрудника (диспетчер или машинист).
 * @property workingDays - рабочие днин сотрудника.
 * @property login - логин сотрудника.
 * @property password - пароль сотрудника.
 * @property routeIds - список маршрутов, за которые отвечает сотрудник.
 */
@Serializable
data class Employee(val id: Int? = null, val name: String, val role: Role,
                    val workingDays: List<Int>? = mutableListOf(), val login: String,
                    val password: String, val routeIds: List<Int>? = mutableListOf()
)

/**
 * Класс для создания интерфейса между API БД.
 * @property connection - соединенние с БД.
 * Реализует интерфейс @see Service.
 */
class EmployeeService(private val connection: Connection): Service {

    /**
     * Статические переменные класса.
     */
    companion object {
        private const val CREATE_TABLE_EMPLOYEES =
            "CREATE TABLE IF NOT EXISTS employees (id SERIAL PRIMARY KEY, fullname TEXT NOT NULL," +
                    "role_id INTEGER REFERENCES roles(id)," +
                    "working_days integer[] NOT NULL DEFAULT array[]::integer[], login TEXT NOT NULL, password TEXT NOT NULL, route_ids integer[] NOT NULL DEFAULT array[]::integer[]);"
        private const val SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employees WHERE id = ?;"
        private const val SELECT_EMPLOYEE = "SELECT * FROM employees;"
        private const val INSERT_EMPLOYEE = "INSERT INTO employees (fullname, role_id, working_days, login, password, route_ids)" +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id;"
        private const val UPDATE_EMPLOYEE = "UPDATE employees SET fullname = ?, role_id = ?, working_days = ?, login = ?, password = ?, role_ids = ?" +
                " WHERE id = ?;"
        private const val DELETE_EMPLOYEE = "DELETE FROM employees WHERE id = ?"

    }

    /**
     * Метод, срабатывающий при инициализации объекта.
     */
    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_EMPLOYEES)
    }

    /**
     * Метод получения всех сотрудников из БД.
     * @return возвращает список объектов типа @see Employee.
     */
     override suspend fun getAll(): List<Employee> = withContext(Dispatchers.IO){
         val statement = connection.prepareStatement(SELECT_EMPLOYEE)
         val resultSet = statement.executeQuery()

         val rolesList = RoleService(connection).getAll()
         val eList = mutableListOf<Employee>()
         while(resultSet.next()) {
             val id = resultSet.getInt("id")
             val fullname = resultSet.getString("fullname")
             val roleId = resultSet.getInt("role_id")
             val workingDays = Helper.convertSQLArrayToIntList(resultSet, "working_days")
             val login = resultSet.getString("login")
             val password = resultSet.getString("password")
             val routeIds = Helper.convertSQLArrayToIntList(resultSet, "route_ids")

             val role = rolesList.find { it.id == roleId }
             role?.let {
                 eList.add(Employee(id, fullname, role, workingDays, login, password, routeIds))
             }
         }
         return@withContext eList
    }

    /**
     * Метод получения объекта из БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     * @return возвращает объект типа @see Employee.
     * @throws Exception("No employees with id=$id found").
     */
     override suspend fun getById(id: Int): Employee = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID)
        statement.setInt(1, id)

        val resultSet = statement.executeQuery()

        if (resultSet.next()) {

            val fullname = resultSet.getString("fullName")
            val roleId = resultSet.getInt("role_id")
            val workingDays = Helper.convertSQLArrayToIntList(resultSet, "working_days")
            val login = resultSet.getString("login")
            val password = resultSet.getString("password")

            val rolesList = RoleService(connection).getAll()
            val role = rolesList.find { it.id == roleId }
            val routeIds = Helper.convertSQLArrayToIntList(resultSet, "route_ids")

            role?.let {
                return@withContext Employee(id, fullname, it, workingDays, login, password, routeIds)
            }
            throw Exception("No employees with id=$id found")
        }
        else {
            throw Exception("No employees with id=$id found")
        }
    }

    /**
     * Метод создания нового объекта в БД.
     * @param obj - объект типа @see Employee.
     * @return возвращает уникальный идентификатор созданного объекта.
     * @throws Exception("error in creating employee").
     */
     override suspend fun create(obj: Any): Int = withContext(Dispatchers.IO) {
         if (obj is Employee) {
             val statement = connection.prepareStatement(INSERT_EMPLOYEE)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.role.id)
             statement.setArray(3, obj.workingDays?.let { Helper.prepareSQLArrayFromIntList(connection, it) })
             statement.setString(4, obj.login)
             statement.setString(5, obj.password)
             statement.setArray(6, obj.routeIds?.let { Helper.prepareSQLArrayFromIntList(connection, it) })
             statement.executeQuery()
             RoleService(connection).create(obj.role)
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

    /**
     * Метод обновления данного объекта в БД.
     * @param obj - объект типа @see Employee.
     * @throws Exception("error in updating employee")
     */
     override suspend fun update(obj: Any): Unit = withContext(Dispatchers.IO) {
         if (obj is Employee) {
             val statement = connection.prepareStatement(UPDATE_EMPLOYEE)
             statement.setString(1, obj.name)
             statement.setInt(2, obj.role.id)
             statement.setArray(3, obj.workingDays?.let { Helper.prepareSQLArrayFromIntList(connection, it) })
             statement.setString(4, obj.login)
             statement.setString(5, obj.password)
             statement.setArray(6, obj.routeIds?.let { Helper.prepareSQLArrayFromIntList(connection, it) })
             statement.executeUpdate()
             RoleService(connection).update(obj.role)
         } else {
             throw Exception("error in updating employee")
         }
    }

    /**
     * Метод удаления объекта в БД по уникальному идентификатору.
     * @param id - уникальный идентификатор объекта.
     */
     override suspend fun delete(id: Int): Unit = withContext(Dispatchers.IO) {
         val statement = connection.prepareStatement(DELETE_EMPLOYEE)
         statement.setInt(1, id)
         statement.executeUpdate()
    }

    /**
     * Метод авторизации пользователя типа @see UserForAuth.
     * @param obj - пользователь, которого необходимо авторизовать.
     * @return возвращает уникальный идентификатор авторизованного пользователя (сотрудника) или 0, если объект (сотрудника) не удалось авторизовать.
     */
    suspend fun authEmployee(obj: UserForAuth): Int = withContext(Dispatchers.IO) {
        val statement =connection.prepareStatement("SELECT id, password FROM employees WHERE login=?;")
        statement.setString(1, obj.login)
        statement.executeQuery()
        val resultSet = statement.resultSet
        if (resultSet.next()) {
            val id = resultSet.getInt(1)
            val pwd = resultSet.getString(2)
            if (obj.password == pwd) {
                return@withContext id
            } else {
                return@withContext 0
            }
        } else {
            return@withContext 0
        }
    }
}