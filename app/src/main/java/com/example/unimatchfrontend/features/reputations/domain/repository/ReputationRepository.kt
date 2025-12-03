package com.example.unimatchfrontend.features.reputations.domain.repository

import com.example.unimatchfrontend.features.reputations.domain.model.Reputation

interface ReputationRepository {
    suspend fun getAllReputations(): List<Reputation>
    suspend fun createReputation(reputation: Reputation): Reputation?
    suspend fun updateReputation(id: Int, reputation: Reputation): Reputation?
    suspend fun getReputationById(id: Int): Reputation?
}
