package com.example.unimatchfrontend.features.projects.data.api

import com.example.unimatchfrontend.features.projects.domain.model.Project
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ProjectApi {

    @GET("projects")
    suspend fun getProjects(): List<Project>

    @POST("projects")
    suspend fun createProject(@Body project: Project): Project

    @PATCH("projects/{id}")
    suspend fun updatePostulants(
        @Path("id") projectId: Int,
        @Body updatedFields: Map<String, Any>
    ): Project


}
