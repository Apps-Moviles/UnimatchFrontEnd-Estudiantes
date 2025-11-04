package com.example.unimatchfrontend.features.projects.domain.model

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val companyId: Int,
    val studentsSelected: List<Int> = emptyList(),
    val isFinished: Boolean = false,
    val postulants: List<Int> = emptyList(),
    val field: String,
    val budget: Double,
    val createdAt: String,
    val requirements: List<String> = emptyList(),
    val status: String // ej: "activo", "cerrado", "en revisión"
)
