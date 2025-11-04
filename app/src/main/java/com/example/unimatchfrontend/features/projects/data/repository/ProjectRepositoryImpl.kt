package com.example.unimatchfrontend.features.projects.data.repository

import com.example.unimatchfrontend.features.projects.data.api.ProjectApi
import com.example.unimatchfrontend.features.projects.domain.model.Project
import com.example.unimatchfrontend.features.projects.domain.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val api: ProjectApi
) : ProjectRepository {

    override suspend fun getAllProjects(): List<Project> {
        return api.getProjects()
    }

    override suspend fun createProject(project: Project): Project? {
        return api.createProject(project)
    }


}
