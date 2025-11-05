package com.example.unimatchfrontend.features.students.data.repository

import com.example.unimatchfrontend.features.students.data.api.StudentApi
import com.example.unimatchfrontend.features.students.domain.model.Student
import com.example.unimatchfrontend.features.students.domain.repository.StudentRepository

class StudentRepositoryImpl(
    private val api: StudentApi
) : StudentRepository {

    override suspend fun getStudentByUserId(userId: Int): Student? {
        return api.getStudentByUserId(userId).firstOrNull()
    }

    override suspend fun getAllStudents(): List<Student> {
        return api.getAllStudents()
    }

    override suspend fun createStudent(student: Student): Student {
        return api.createStudent(student)
    }
}
