package com.example.plugins

import java.sql.Array
import java.sql.Connection
import java.sql.ResultSet

/**
 * Вспомогательный класс со статическими методами для работы с БД.
 */
class Helper {
    companion object {
        /**
         * Статический метод для преобразования списка целых чисел в массив SQL.
         * @param connection - соединение с БД.
         * @param list - список целых чисел.
         * @return объект типа java.SQL.Array.
         */
        fun prepareSQLArrayFromIntList(connection: Connection, list: List<Int>): Array {
            return connection.createArrayOf("integer", list.toTypedArray())
        }

        /**
         * Статический метод для преобразования списка строк в массив SQL.
         * @param connection - соединение с БД.
         * @param list - список строк.
         * @return объект типа java.SQL.Array.
         */
        fun prepareSQLArrayFromStringList(connection: Connection, list: List<String>): Array {
            return connection.createArrayOf("text", list.toTypedArray())
        }

        /** Статический метод для преобразования массива SQL в список целых чисел.
         * @param resultSet - набор данных, полученных из БД.
         * @param arrayName - имя поля в БД, которое необходимо преобразовать.
         * @return список целых чисел.
         */
        fun convertSQLArrayToIntList(resultSet: ResultSet, arrayName: String): List<Int> {
            val rs = resultSet.getArray(arrayName).resultSet
            val workingDays = mutableListOf<Int>()
            while (rs.next()) {
                workingDays.add(rs.getInt(2))
            }
            return workingDays
        }

        /** Статический метод для преобразования массива SQL в список строк.
         * @param resultSet - набор данных, полученных из БД.
         * @param arrayName - имя поля в БД, которое необходимо преобразовать.
         * @return список строк.
         */
        fun convertSQLArrayToStringList(resultSet: ResultSet, arrayName: String): List<String> {
            val rs = resultSet.getArray(arrayName).resultSet
            val workingDays = mutableListOf<String>()
            while (rs.next()) {
                workingDays.add(rs.getString(2))
            }
            return workingDays
        }
    }
}