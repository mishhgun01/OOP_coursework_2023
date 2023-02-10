package com.example.plugins

import java.sql.Array
import java.sql.Connection
import java.sql.ResultSet

class SQLHelper {
    companion object {
        fun prepareArrayFromIntList(connection: Connection, list: List<Int>): Array {
            return connection.createArrayOf("integer", list.toTypedArray())
        }

        fun prepareArrayFromStringList(connection: Connection, list: List<String>): Array {
            return connection.createArrayOf("text", list.toTypedArray())
        }
            fun convertToIntList(resultSet: ResultSet, arrayName: String): List<Int> {
                val rs = resultSet.getArray(arrayName).getResultSet()
                val workingDays = mutableListOf<Int>()
                while (rs.next()) {
                    workingDays.add(rs.getInt(1))
                }
                return workingDays
            }

            fun convertToStringList(resultSet: ResultSet, arrayName: String): List<String> {
                val rs = resultSet.getArray(arrayName).getResultSet()
                val workingDays = mutableListOf<String>()
                while (rs.next()) {
                    workingDays.add(rs.getString(1))
                }
                return workingDays
            }

    }
}