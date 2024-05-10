package com.example.pumproject.databaseConnection

class UserRepository {
    private val api = RetrofitInstance.api
    suspend fun getUser(name : String) : List<User>{
        return api.user(name)
    }
}