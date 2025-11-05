package com.example.unimatchfrontend.features.projects.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimatchfrontend.features.projects.domain.model.Project
import com.example.unimatchfrontend.features.projects.domain.usecase.GetAllProjectsUseCase
import com.example.unimatchfrontend.features.projects.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val repository: ProjectRepository
) : ViewModel() {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects

    fun loadProjects() {
        viewModelScope.launch {
            _projects.value = getAllProjectsUseCase()
        }
    }

    fun postulateToProject(
        project: Project,
        studentId: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedPostulants = project.postulants.toMutableList()

                if (updatedPostulants.contains(studentId)) {
                    onResult(false, "Ya estás postulado a este proyecto.")
                    return@launch
                }

                updatedPostulants.add(studentId)

                val updatedProject = repository.updateProjectPostulants(project.id, updatedPostulants)

                if (updatedProject != null) {
                    // Actualiza localmente el proyecto en el listado
                    val current = _projects.value.toMutableList()
                    val index = current.indexOfFirst { it.id == project.id }
                    if (index != -1) {
                        current[index] = updatedProject
                        _projects.value = current
                    }
                    onResult(true, "¡Postulación enviada correctamente!")
                } else {
                    onResult(false, "Error al actualizar el proyecto.")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }
}
