package com.example.unimatchfrontend.features.reputations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimatchfrontend.features.reputations.domain.model.Reputation
import com.example.unimatchfrontend.features.reputations.domain.repository.ReputationRepository
import kotlinx.coroutines.launch

class ReputationViewModel(
    private val repository: ReputationRepository
) : ViewModel() {

    var lastCreatedReputation: Reputation? = null
        private set

    suspend fun getAllReputations(): List<Reputation> {
        return repository.getAllReputations()
    }

    fun createReputation(
        studentId: Int,
        projectId: Int,
        rating: Double,
        comment: String,
        type: Int,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // autoincrement ID manual porque usamos db.json
                val all = repository.getAllReputations()
                val nextId = (all.maxOfOrNull { it.id ?: 0 } ?: 0) + 1

                val new = Reputation(
                    id = nextId,
                    studentId = studentId,
                    projectId = projectId,
                    rating = rating,
                    comment = comment,
                    type = type
                )

                val saved = repository.createReputation(new)

                if (saved != null) {
                    lastCreatedReputation = saved
                    onResult(true)
                } else {
                    onResult(false)
                }
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }
}
