package com.example.unimatchfrontend.features.users.data.api

import com.example.unimatchfrontend.features.users.domain.model.User
import retrofit2.http.*

interface UserApi {

    @GET("users")
    suspend fun getUsers(): List<User>

    @POST("users")
    suspend fun createUser(@Body user: User): User

    @GET("users")
    suspend fun getAllUsers(): List<User>
}
