package com.example.unimatchfrontend.features.studentpostulations.domain.model

data class StudentPostulation(
    val id: Int? = null,
    val studentId: Int,
    val projectId: Int,
    val status: String, // "Pendiente", "Aceptado", "Rechazado"
    val date: String    // formato: "YYYY-MM-DD"
)
