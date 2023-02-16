package com.example.model

import kotlinx.serialization.Serializable

/**
 * Класс пользователя для авторизации.
 * @property login - список маршрутов, за которые отвечает сотрудник.
 * @property password - пароль сотрудника.
 */
@Serializable
data class UserForAuth(val login: String, val password: String)
