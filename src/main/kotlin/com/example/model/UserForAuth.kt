package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserForAuth(val login: String, val password: String)
