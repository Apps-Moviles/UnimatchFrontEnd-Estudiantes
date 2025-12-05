package com.example.unimatchfrontend.features.reputations.data.repository

import com.example.unimatchfrontend.features.reputations.data.api.ReputationApi
import com.example.unimatchfrontend.features.reputations.domain.model.Reputation
import com.example.unimatchfrontend.features.reputations.domain.repository.ReputationRepository

class ReputationRepositoryImpl(
    private val api: ReputationApi
) : ReputationRepository {

    override suspend fun getAllReputations(): List<Reputation> {
        return api.getAllReputations()
    }

    override suspend fun createReputation(reputation: Reputation): Reputation? {
        return api.createReputation(reputation)
    }

    override suspend fun updateReputation(id: Int, reputation: Reputation): Reputation? {
        return api.updateReputation(id, reputation)
    }

    override suspend fun getReputationById(id: Int): Reputation? {
        return api.getReputationById(id)
    }
}
