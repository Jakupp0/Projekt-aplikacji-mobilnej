package com.example.pumproject.databaseConnection

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("php1/index.php/user/CheckUser")
    suspend fun getUser(@Query("name") name :String,@Query("passwd") passwd: String ): List<User>
    @GET("php1/index.php/user/AddUser?name=jakupp&passwd=janek")
    suspend fun AddUser(): Int
}