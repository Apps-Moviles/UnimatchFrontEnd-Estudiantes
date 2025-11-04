package com.example.unimatchfrontend.features.users.domain.usecase

import com.example.unimatchfrontend.features.users.domain.model.User
import com.example.unimatchfrontend.features.users.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): User? {
        return repository.login(email, password)
    }
}
