package com.example.unimatchfrontend.features.students.data.api

import com.example.unimatchfrontend.features.students.domain.model.Student
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StudentApi {
    @GET("students")
    suspend fun getStudentByUserId(@Query("userId") userId: Int): List<Student>

    @GET("students")
    suspend fun getAllStudents(): List<Student>

    @POST("students")
    suspend fun createStudent(@Body student: Student): Student

    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id: Int, @Body student: Student): Student



}
