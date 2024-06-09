package com.example.pumproject

enum class TypeOfPlace
{
    PRIVATE, PUBLIC
}

data class Place(
    val PlaceId: Int,
    val Name: String,
    val Latitude: String,
    val Longitude: String,
    val Description: String,
    val Type: TypeOfPlace,
    val UserName: String
)