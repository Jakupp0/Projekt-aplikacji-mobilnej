package com.example.pumproject.databaseConnection

import com.google.gson.annotations.SerializedName

data class Friend(
    @SerializedName("Username1")
    var username1 : String="",
    @SerializedName("Username2")
    var username2: String ="",
    @SerializedName("CreatedAt")
    var createdate: String ="",
    @SerializedName("Stage")
    var stage: String=""
)

