package com.example.pumproject.databaseConnection

import retrofit2.http.GET
import retrofit2.http.Path

interface PlaceholderApi {
    @GET("CheckUser?name={idname}")
    suspend fun user(@Path("idname") idname : String): List<User>
}