package com.example.plugins

import java.sql.Array
import java.sql.Connection
import java.sql.ResultSet

class Helper {
    companion object {
        fun prepareSQLArrayFromIntList(connection: Connection, list: List<Int>): Array {
            return connection.createArrayOf("integer", list.toTypedArray())
        }

        fun prepareSQLArrayFromStringList(connection: Connection, list: List<String>): Array {
            return connection.createArrayOf("text", list.toTypedArray())
        }
        fun convertSQLArrayToIntList(resultSet: ResultSet, arrayName: String): List<Int> {
            val rs = resultSet.getArray(arrayName).resultSet
            val workingDays = mutableListOf<Int>()
            while (rs.next()) {
                workingDays.add(rs.getInt(2))
            }
            return workingDays
        }

        fun convertSQLArrayToStringList(resultSet: ResultSet, arrayName: String): List<String> {
            val rs = resultSet.getArray(arrayName).resultSet
            val workingDays = mutableListOf<String>()
            while (rs.next()) {
                workingDays.add(rs.getString(2))
            }
            return workingDays
        }

        fun compareHashes(enterPwd: String, pwdGotFromDB: String): Boolean {
            return enterPwd == pwdGotFromDB
        }
    }
}