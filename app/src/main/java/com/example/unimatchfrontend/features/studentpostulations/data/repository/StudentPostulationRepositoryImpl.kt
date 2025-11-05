package com.example.unimatchfrontend.features.studentpostulations.data.repository

import com.example.unimatchfrontend.features.studentpostulations.data.api.StudentPostulationApi
import com.example.unimatchfrontend.features.studentpostulations.domain.model.StudentPostulation
import com.example.unimatchfrontend.features.studentpostulations.domain.repository.StudentPostulationRepository

class StudentPostulationRepositoryImpl(
    private val api: StudentPostulationApi
) : StudentPostulationRepository {

    override suspend fun getAll(): List<StudentPostulation> {
        return api.getAllPostulations()
    }

    override suspend fun getByStudentId(studentId: Int): List<StudentPostulation> {
        return api.getPostulationsByStudentId(studentId)
    }

    override suspend fun getByStudentAndProject(studentId: Int, projectId: Int): StudentPostulation? {
        return api.getPostulationByStudentAndProject(studentId, projectId).firstOrNull()
    }

    override suspend fun create(postulation: StudentPostulation): StudentPostulation {
        return api.createPostulation(postulation)
    }

    override suspend fun getPostulationsByStudentId(studentId: Int): List<StudentPostulation> {
        return api.getPostulationsByStudentId(studentId)
    }





}
