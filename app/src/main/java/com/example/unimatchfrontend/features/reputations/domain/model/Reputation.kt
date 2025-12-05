package com.example.unimatchfrontend.features.reputations.domain.model

data class Reputation(
    val id: Int? = null,
    val studentId: Int,
    val projectId: Int,
    val rating: Double,
    val comment: String,
    val type: Int // 1 = company → student, 2 = student → company
)
