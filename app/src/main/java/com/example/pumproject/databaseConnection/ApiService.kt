package com.example.pumproject.databaseConnection

import android.provider.ContactsContract.Data
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("php1/index.php/user/CheckUser")
    suspend fun getUser(@Query("name") name :String,@Query("passwd") passwd: String ): List<User>
    @GET("php1/index.php/user/AddUser")
    suspend fun AddUser(@Query("name")name :String,@Query("passwd") passwd: String): DatabaseInsert

    @GET("php1/index.php/user/DeleteUser")
    suspend fun DeleteUser(@Query("userId")Id :String): DatabaseDelete

    @GET("php1/index.php/user/UpdateUser")
    suspend fun UpdateUser(@Query("userId")Id :String,@Query("passwd") passwd: String): DatabaseInsert



}