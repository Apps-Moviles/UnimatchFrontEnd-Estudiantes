package com.example.unimatchfrontend.features.studentpostulations.domain.repository

import com.example.unimatchfrontend.features.studentpostulations.domain.model.StudentPostulation

interface StudentPostulationRepository {

    suspend fun getAll(): List<StudentPostulation>

    suspend fun getByStudentId(studentId: Int): List<StudentPostulation>

    suspend fun getByStudentAndProject(studentId: Int, projectId: Int): StudentPostulation?

    suspend fun create(postulation: StudentPostulation): StudentPostulation

    suspend fun getPostulationsByStudentId(studentId: Int): List<StudentPostulation>

}
