package com.example.pumproject.databaseConnection

import android.provider.ContactsContract.Data
import com.example.pumproject.Place
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

    @GET("php1/index.php/friends/CheckFriends")
    suspend fun CheckFriends(@Query("name1")name :String): List<Friend>

    @GET("php1/index.php/friends/DeleteInvitation")
    suspend fun delFriend(@Query("name1")name1 :String,@Query("name2")name2 :String)

    @GET("php1/index.php/friends/SendInvitation")
    suspend fun sendFriend(@Query("name1")name1 :String,@Query("name2")name2 :String)

    @GET("php1/index.php/user/CheckUser")
    suspend fun checkFriend(@Query("name")name :String): List<User>

    @GET("php1/index.php/friends/CheckInvitation")
    suspend fun getInvitation(@Query("name")name :String): List<Friend>

    @GET("php1/index.php/friends/AcceptInvitation")
    suspend fun accInvitation(@Query("name1")name1 :String,@Query("name2")name2 :String)

    @GET("php1/index.php/places/deletePlace")
    suspend fun deletePlace(@Query("id")id :String)
    @GET("php1/index.php/places/getPlaces")
    suspend fun getPlaces(@Query("name")name :String) : MutableList<com.example.pumproject.databaseConnection.Place>

    @GET("php1/index.php/places/getAllplaces")
    suspend fun getAllplaces() : MutableList<com.example.pumproject.databaseConnection.Place>

    @GET("php1/index.php/places/getPlace")
    suspend fun getPlace(@Query("id")id :String)

    @GET("php1/index.php/places/changeVisibility")
    suspend fun changeVisibility(@Query("id")id :String,@Query("type")type :String)


    @GET("php1/index.php/places/addPlace")
    suspend fun addPlace(@Query("name")name :String,@Query("latitude")latitude :String,@Query("longitude")longitude :String,
                         @Query("description")description :String,@Query("type")type :String,@Query("id_Name")id_name :String,)

}