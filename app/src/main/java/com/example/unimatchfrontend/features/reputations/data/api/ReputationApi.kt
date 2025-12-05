package com.example.unimatchfrontend.features.reputations.data.api

import com.example.unimatchfrontend.features.reputations.domain.model.Reputation
import retrofit2.http.*

interface ReputationApi {

    @GET("reputations")
    suspend fun getAllReputations(): List<Reputation>

    @POST("reputations")
    suspend fun createReputation(@Body reputation: Reputation): Reputation?

    @GET("reputations/{id}")
    suspend fun getReputationById(@Path("id") id: Int): Reputation?

    @PUT("reputations/{id}")
    suspend fun updateReputation(
        @Path("id") id: Int,
        @Body reputation: Reputation
    ): Reputation?

    @DELETE("reputations/{id}")
    suspend fun deleteReputation(@Path("id") id: Int)
}
