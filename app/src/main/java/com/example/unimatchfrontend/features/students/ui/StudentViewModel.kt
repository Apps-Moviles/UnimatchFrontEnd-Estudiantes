package com.example.unimatchfrontend.features.students.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimatchfrontend.features.students.domain.model.Student
import com.example.unimatchfrontend.features.students.domain.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    private val _student = MutableStateFlow<Student?>(null)
    val student: StateFlow<Student?> = _student

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadStudentByUserId(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _student.value = repository.getStudentByUserId(userId)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun updateStudent(updatedStudent: Student) {
        val updated = repository.updateStudent(updatedStudent)
        _student.value = updated
    }

    suspend fun getStudentByUserId(userId: Int): Student? {
        return repository.getStudentByUserId(userId)
    }

}
