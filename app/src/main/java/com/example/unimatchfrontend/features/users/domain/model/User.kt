package com.example.unimatchfrontend.features.users.domain.model

data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    val role: String // "student" o "company"
)
