package com.example.unimatchfrontend.features.studentpostulations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimatchfrontend.features.companies.domain.repository.CompanyRepository
import com.example.unimatchfrontend.features.projects.domain.model.Project
import com.example.unimatchfrontend.features.projects.domain.repository.ProjectRepository

import com.example.unimatchfrontend.features.studentpostulations.domain.model.StudentPostulation
import com.example.unimatchfrontend.features.studentpostulations.domain.repository.StudentPostulationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StudentPostulationViewModel(
    private val repository: StudentPostulationRepository,
    private val projectRepository: ProjectRepository,
    private val companyRepository: CompanyRepository

) : ViewModel() {

    private val _postulations = MutableStateFlow<List<StudentPostulation>>(emptyList())
    val postulations: StateFlow<List<StudentPostulation>> = _postulations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _studentPostulations = MutableStateFlow<List<StudentPostulation>>(emptyList())
    val studentPostulations: StateFlow<List<StudentPostulation>> = _studentPostulations


    fun loadPostulationsByStudent(studentId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _postulations.value = repository.getByStudentId(studentId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPostulationByStudentAndProject(
        studentId: Int,
        projectId: Int,
        onResult: (StudentPostulation?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = repository.getByStudentAndProject(studentId, projectId)
                onResult(result)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                onResult(null)
            }
        }
    }

    fun postulate(studentId: Int, projectId: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                // Paso 1: obtener todas las postulaciones
                val allPostulations = repository.getAll()

                // Paso 2: buscar el mayor ID existente
                val maxId = allPostulations.mapNotNull { it.id }.maxOrNull() ?: 0
                val newId = maxId + 1

                // Paso 3: crear nueva postulación con ID único
                val postulation = StudentPostulation(
                    id = newId,
                    studentId = studentId,
                    projectId = projectId,
                    status = "Pendiente",
                    date = System.currentTimeMillis().toString()
                )

                // Paso 4: crear en el repositorio
                repository.create(postulation)
                onResult(true, "¡Postulación enviada correctamente!")
            } catch (e: Exception) {
                onResult(false, "Error al postular: ${e.message}")
            }
        }
    }

    fun getPostulationsByStudentId(studentId: Int) {
        viewModelScope.launch {
            val result = repository.getPostulationsByStudentId(studentId)
            _studentPostulations.value = result
        }
    }

    fun getProjectById(projectId: Int): Project? {
        return runBlocking {
            projectRepository.getProjectById(projectId)
        }
    }

    fun getCompanyNameByProjectId(projectId: Int): String {
        return runBlocking {
            val project = projectRepository.getProjectById(projectId)
            val companyId = project?.companyId
            if (companyId != null) {
                val company = companyRepository.getCompanyById(companyId)
                company?.companyName ?: "Empresa desconocida"
            } else {
                "Empresa desconocida"
            }
        }
    }


}
