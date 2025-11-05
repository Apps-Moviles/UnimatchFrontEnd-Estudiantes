package com.example.unimatchfrontend.features.studentpostulations.data.api

import com.example.unimatchfrontend.features.studentpostulations.domain.model.StudentPostulation
import retrofit2.http.*

interface StudentPostulationApi {

    @GET("studentPostulations")
    suspend fun getAllPostulations(): List<StudentPostulation>

    @GET("studentPostulations")
    suspend fun getPostulationsByStudentId(
        @Query("studentId") studentId: Int
    ): List<StudentPostulation>

    @GET("studentPostulations")
    suspend fun getPostulationByStudentAndProject(
        @Query("studentId") studentId: Int,
        @Query("projectId") projectId: Int
    ): List<StudentPostulation> // retorna lista pero asumimos solo 1 coincidencia

    @POST("studentPostulations")
    suspend fun createPostulation(
        @Body postulation: StudentPostulation
    ): StudentPostulation


}
