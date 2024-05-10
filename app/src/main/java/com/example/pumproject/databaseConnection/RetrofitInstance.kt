package com.example.pumproject.databaseConnection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: PlaceholderApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost/php1/index.php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceholderApi::class.java)
    }
}