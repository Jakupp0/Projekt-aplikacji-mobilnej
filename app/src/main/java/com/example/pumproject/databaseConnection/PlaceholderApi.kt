package com.example.pumproject.databaseConnection

import retrofit2.http.GET

interface PlaceholderApi {
    @GET("user")
    suspend fun user(): User
}