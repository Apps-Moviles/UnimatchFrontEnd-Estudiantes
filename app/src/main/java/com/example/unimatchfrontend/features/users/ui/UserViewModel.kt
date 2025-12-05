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
import com.example.unimatchfrontend.features.users.domain.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    var currentUser by mutableStateOf<User?>(null)
        private set

    var currentStudent by mutableStateOf<Student?>(null)

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

    fun registerStudentWithUser(
        user: User,
        birthdate: String,
        city: String,
        country: String,
        career: String,
        phoneNumber: String
    ) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val allUsers = userRepository.getAllUsers()
                val allStudents = studentRepository.getAllStudents()

                val nextUserId = (allUsers.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
                val nextStudentId = (allStudents.maxOfOrNull { it.id ?: 0 } ?: 0) + 1

                val cleanUser = user.copy(id = nextUserId)
                val createdUser = registerUseCase(cleanUser)

                if (createdUser != null) {
                    currentUser = createdUser

                    val newStudent = Student(
                        id = nextStudentId,
                        userId = createdUser.id,
                        birthdate = birthdate,
                        city = city,
                        country = country,
                        career = career,
                        phoneNumber = phoneNumber,
                        portfolioLink = "",
                        aboutMe = "",
                        rating = 0.0,
                        profilePicture = "",
                        endedProjects = emptyList()
                    )

                    val createdStudent = studentRepository.createStudent(newStudent)
                    currentStudent = createdStudent
                } else {
                    errorMessage = "Error al crear usuario"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun registerCompanyWithUser(
        user: User,
        companyName: String,
        sector: String,
        location: String,
        phone: String
    ) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                // Obtener todos los usuarios (students + companies)
                val allUsers = userRepository.getAllUsers()
                val allCompanies = companyRepository.getAllCompanies()

                val nextUserId = (allUsers.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
                val nextCompanyId = (allCompanies.maxOfOrNull { it.id ?: 0 } ?: 0) + 1

                val cleanUser = user.copy(id = nextUserId)
                val createdUser = registerUseCase(cleanUser)

                if (createdUser != null) {
                    currentUser = createdUser

                    val newCompany = Company(
                        id = nextCompanyId,
                        userId = createdUser.id,
                        companyName = companyName,
                        sector = sector,
                        location = location,
                        email = createdUser.email,
                        phone = phone,
                        rating = 0.0,
                        profilePicture = "",
                        description = ""
                    )

                    val createdCompany = companyRepository.createCompany(newCompany)
                    currentCompany = createdCompany
                } else {
                    errorMessage = "Error al crear usuario"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }


    suspend fun getUserById(id: Int?): User? {
        return userRepository.getUserById(id)
    }

    suspend fun getStudentNameByStudentId(studentId: Int): String {
        return try {
            val students = studentRepository.getAllStudents()
            val student = students.firstOrNull { it.id == studentId }

            if (student != null) {
                val user = userRepository.getUserById(student.userId)
                user?.name ?: "Estudiante"
            } else {
                "Estudiante"
            }
        } catch (e: Exception) {
            "Estudiante"
        }
    }


    fun logout() {
        currentUser = null
        currentStudent = null
    }

    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            try {
                val updated = userRepository.updateUser(updatedUser)
                currentUser = updated
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }








}
