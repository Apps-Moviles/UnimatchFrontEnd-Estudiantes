package com.example.unimatchfrontend.features.projects.domain.repository

import com.example.unimatchfrontend.features.projects.domain.model.Project

interface ProjectRepository {
    suspend fun getAllProjects(): List<Project>
    suspend fun createProject(project: Project): Project?
}
