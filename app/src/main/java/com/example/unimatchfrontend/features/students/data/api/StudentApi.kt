package com.example.unimatchfrontend.features.students.data.api

import com.example.unimatchfrontend.features.students.domain.model.Student
import retrofit2.http.GET
import retrofit2.http.Query

interface StudentApi {
    @GET("students")
    suspend fun getStudentByUserId(@Query("userId") userId: Int): List<Student>
}
