package com.example.unimatchfrontend.features.users.domain.repository

import com.example.unimatchfrontend.features.users.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun register(user: User): User?

    suspend fun getAllUsers(): List<User>

    suspend fun getUserById(id: Int?): User?

    suspend fun updateUser(user: User): User

}
