package com.example.unimatchfrontend.features.projects.domain.repository

import com.example.unimatchfrontend.features.projects.domain.model.Project

interface ProjectRepository {
    suspend fun getAllProjects(): List<Project>

    suspend fun createProject(project: Project): Project?

    suspend fun updateProjectPostulants(projectId: Int, postulants: List<Int>): Project?

    suspend fun getProjectById(id: Int): Project?

    suspend fun updateProject(project: Project): Project?

}
