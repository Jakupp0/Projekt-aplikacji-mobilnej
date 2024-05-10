package com.example.pumproject.databaseConnection

class UserRepository {
    private val api = RetrofitInstance.api
    suspend fun getUser() : User{
        return api.user()
    }
}