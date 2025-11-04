package com.example.unimatchfrontend.features.companies.domain.model

data class Company(
    val id: Int? = null,
    val userId: Int? = null,
    val companyName: String = "",
    val sector: String = "",
    val location: String = "",
    val email: String = "",
    val phone: String = "",
    val rating: Double = 0.0,
    val profilePicture: String = "",
    val description: String = ""
)
