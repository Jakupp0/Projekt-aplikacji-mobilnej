package com.example.pumproject.databaseConnection

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("PlaceId")
    val PlaceId: Int,
    @SerializedName("Name")
    val Name: String,
    @SerializedName("Latitude")
    val Latitude: String,
    @SerializedName("Longitude")
    val Longitude: String,
    @SerializedName("Description")
    val Description: String,
    @SerializedName("Type")
    val Type: TypeOfPlace,
    @SerializedName("Id_name")
    val UserName: String,
    @SerializedName("CreatedAt")
    val CreatedAt: String
)
