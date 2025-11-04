package com.example.unimatchfrontend.features.users.domain.usecase

import com.example.unimatchfrontend.features.users.domain.model.User
import com.example.unimatchfrontend.features.users.domain.repository.UserRepository

class RegisterUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User): User? {
        return repository.register(user)
    }
}
