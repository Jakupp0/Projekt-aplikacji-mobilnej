package com.example.pumproject.databaseConnection
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("UserId")
    val userId : Int,
    @SerializedName("Username")
    val nickname: String,
    @SerializedName("HashPassword")
    val hashPassword: String,
    @SerializedName("CreatedAt")
    val createdAt: String
)
