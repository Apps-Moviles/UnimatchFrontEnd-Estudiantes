package com.example.unimatchfrontend.features.users.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimatchfrontend.features.users.domain.model.User
import com.example.unimatchfrontend.features.users.domain.usecase.LoginUseCase
import com.example.unimatchfrontend.features.users.domain.usecase.RegisterUseCase
import com.example.unimatchfrontend.features.students.domain.model.Student
import com.example.unimatchfrontend.features.companies.domain.model.Company
import com.example.unimatchfrontend.features.students.domain.repository.StudentRepository
import com.example.unimatchfrontend.features.companies.domain.repository.CompanyRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val studentRepository: StudentRepository,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    var currentUser by mutableStateOf<User?>(null)
        private set

    var currentStudent by mutableStateOf<Student?>(null)
        private set

    var currentCompany by mutableStateOf<Company?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val user = loginUseCase(email, password)
            if (user != null) {
                currentUser = user
                loadProfileForUser(user.id, user.role)
            } else {
                errorMessage = "Credenciales inválidas"
            }
            isLoading = false
        }
    }

    fun register(user: User) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val createdUser = registerUseCase(user)
            if (createdUser != null) {
                currentUser = createdUser
                loadProfileForUser(createdUser.id, createdUser.role)
            } else {
                errorMessage = "No se pudo registrar"
            }
            isLoading = false
        }
    }

    private suspend fun loadProfileForUser(userId: Int?, role: String) {
        if (userId == null) return // si por alguna razón no tiene id, evita crashear

        when (role.lowercase()) {
            "student" -> {
                currentStudent = studentRepository.getStudentByUserId(userId)
            }
            "company" -> {
                currentCompany = companyRepository.getCompanyByUserId(userId)
            }
        }
    }
}
