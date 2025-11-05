package com.example.unimatchfrontend.features.users.data.repository

import com.example.unimatchfrontend.features.users.data.api.UserApi
import com.example.unimatchfrontend.features.users.domain.model.User
import com.example.unimatchfrontend.features.users.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    override suspend fun login(email: String, password: String): User? {
        val users = api.getUsers()
        return users.find { it.email == email && it.password == password }
    }

    override suspend fun register(user: User): User? {
        return api.createUser(user)
    }

    override suspend fun getAllUsers(): List<User> {
        return api.getAllUsers()
    }

    override suspend fun getUserById(id: Int?): User? {
        return api.getUserById(id)
    }

    override suspend fun updateUser(user: User): User {
        return api.updateUser(user.id ?: throw IllegalArgumentException("ID is null"), user)
    }

}
