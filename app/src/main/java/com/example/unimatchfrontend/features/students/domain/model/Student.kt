package com.example.unimatchfrontend.features.students.domain.model

data class Student(
    val id: Int? = null,
    val userId: Int? = null,
    val birthdate: String = "",
    val city: String = "",
    val country: String = "",
    val career: String = "",
    val phoneNumber: String = "",
    val portfolioLink: String = "",
    val aboutMe: String = "",
    val rating: Double = 0.0,
    val profilePicture: String = "",
    val endedProjects: List<Int> = emptyList()
)
