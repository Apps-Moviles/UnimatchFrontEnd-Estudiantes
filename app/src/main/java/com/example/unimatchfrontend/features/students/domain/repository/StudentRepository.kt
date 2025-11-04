package com.example.unimatchfrontend.features.students.domain.repository

import com.example.unimatchfrontend.features.students.domain.model.Student

interface StudentRepository {
    suspend fun getStudentByUserId(userId: Int): Student?
}
