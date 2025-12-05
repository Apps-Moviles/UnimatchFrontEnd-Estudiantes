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
                val currentProject = repository.getProjectById(project.id)

                if (currentProject == null) {
                    onResult(false, "No se encontró el proyecto.")
                    return@launch
                }

                val updatedPostulants = currentProject.postulants.toMutableList()
                if (!updatedPostulants.contains(studentId)) {
                    updatedPostulants.add(studentId)
                }

                val updatedProject = currentProject.copy(postulants = updatedPostulants)

                val saved = repository.updateProject(updatedProject)

                if (saved != null) {
                    val list = _projects.value.toMutableList()
                    val index = list.indexOfFirst { it.id == saved.id }
                    if (index != -1) {
                        list[index] = saved
                        _projects.value = list
                    }
                    onResult(true, "¡Actualizado correctamente!")
                } else {
                    onResult(false, "Error al guardar cambios.")
                }

            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }

}
