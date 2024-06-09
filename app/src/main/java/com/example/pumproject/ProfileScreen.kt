package com.example.pumproject

import androidx.compose.runtime.*
import android.content.Context
import android.content.Intent
import android.os.SystemClock.sleep
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pumproject.databaseConnection.ApiClient
import com.example.pumproject.databaseConnection.Friend
import com.example.pumproject.databaseConnection.State
import com.example.pumproject.databaseConnection.User
import com.example.pumproject.databaseConnection.UserInfo


@Composable
fun ProfileScreen(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") { ProfileScreen(navController) }
        composable("addFriendScreen") { AddFriendScreen(navController) }
        composable("deleteScreen") { deleteScreen(navController,context) }
        composable("manageAccountScreen") { ManageAccountScreen(navController,context) }
        composable("changePasswordScreen") { changePasswordScreen(navController,context) }
        composable("friendsScreen") { FriendsScreen(modifier = Modifier,navController) }
        composable("managefriendsScreen") { friendsManager(navController) }


    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with user image and name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.person), // replace with your image resource
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userInformation.name,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Aktualności")
            Spacer(modifier = Modifier.height(32.dp))
            // Placeholder for RecyclerView
            LazyColumn(
                modifier = Modifier.height(300.dp)
            ) {
                items(30) { index ->
                    Text(text = "Item $index")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons for navigation
            Button(
                onClick = { navController.navigate("managefriendsScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj znajomych")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("friendsScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lista znajomych")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("manageAccountScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zarządzanie kontem")
            }
        }
    }
}

@Composable
fun AddFriendScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Dodaj znajomych", fontSize = 24.sp)
    }
}



@Composable
fun ManageAccountScreen(navController: NavHostController,context: Context) {
    Column() {


        Text(text = "Zarządzanie kontem", fontSize = 24.sp)
        Button(
            onClick = { navController.navigate("changePasswordScreen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zmień hasło")
        }

        Button(
            onClick = { navController.navigate("deleteScreen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Usuń konto")
        }
        Button(
            onClick = { logout(navController,context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Wyloguj się")
        }
    }

}
@Composable
fun deleteScreen(navController: NavHostController,context: Context){
    var deleteAccount by remember { mutableStateOf(false) }

    Column(){
        Text(text = "Jesteś pewny?")
        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = { deleteAccount = true }) {
                Text("Tak")
            }
            Button(onClick = { navController.navigate("manageAccountScreen") }) {
                Text("Nie")
            }
        }
    }
    LaunchedEffect(deleteAccount) {
        if (deleteAccount == true) {
            delete()
            sleep(2000)
            reload(context)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun changePasswordScreen(navController: NavHostController,context: Context){
    var password by remember {
        mutableStateOf(String())
    }
    var change: Boolean by remember { mutableStateOf(false) }
    var feedback : String by remember {
        mutableStateOf("")
    }

    Column(){

        Text(text = feedback.toString())
        Text(text = "Wprowadź nowe haslo: ")
        TextField(
            value = password,
            onValueChange = { password = it

            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 20.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)
        )
        Button(onClick = { change = true }) {
            Text("Tak")
            
        }


    }
    LaunchedEffect(change) {
        if (change == true) {
            if(password.length > 5){
            change(password)
            feedback = "Zaaktualizowano pomyslnie" }
            else{
                feedback = "Haslo jest za krotkie"
            }

        }
    }

}
fun logout(navController: NavHostController,context: Context){
    reload(context)
}

suspend fun change(newpassword: String){
    val apiService = ApiClient.create()
    val id = userInformation.Id.toString()
    apiService.UpdateUser(id,newpassword)
}

fun reload(context: Context){
    val ctx: Context = context
    val pm = ctx.packageManager
    val intent = pm.getLaunchIntentForPackage(ctx.packageName)
    val mainIntent = Intent.makeRestartActivityTask(intent!!.component)
    ctx.startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}
suspend fun delete() {
        val apiService = ApiClient.create()
        val id = userInformation.Id.toString()
        apiService.DeleteUser(id)
}
@Composable
fun FriendsScreen(modifier: Modifier, navController: NavHostController) {
    val friends = remember { mutableStateListOf<Friend>() }

    Column(modifier = modifier.padding(bottom = 80.dp)) {
        Text(text = "Lista znajomych")
        Spacer(modifier = Modifier.height(60.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp)
        ) {
            itemsIndexed(friends) { index, friend ->
                ListItem(friend)
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }

    LaunchedEffect(Unit) {
        val fetchedFriends = getFriends()
        friends.clear()
        friends.addAll(fetchedFriends.filter { it.stage == "Accepted" })
        println(friends.size)

    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(item: Friend) {
    var field1 by remember { mutableStateOf(item.username2) }
    var field2 by remember { mutableStateOf(item.createdate) }
    var delete : Boolean by remember {mutableStateOf(false)}
    if(item.username1== userInformation.name)
        field1 = item.username2
    else{
        field1 = item.username1
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, Color.Blue)

    ) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.person), // replace with your image resource
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White)
            )

            Text(
                text = "Nazwa: " +field1, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))

      
        }
        Text(
            text = "Znajomość od: "+field2, modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { delete = true ; field1 = "Usunięty" ; field2 =" "}) {
            Text(text = "Usuń znajomego")
        }

    }
    LaunchedEffect(delete) {
        if(delete==true){
            deleteFriend(item.username2)
        }
    }
}


@Composable
fun PendingItem(item: Friend) {
    var field1 by remember { mutableStateOf(item.username1) }
    var add : Boolean by remember {mutableStateOf(false)}
    var added : Boolean by remember {mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, Color.Blue)

    ) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.person), // replace with your image resource
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White)
            )

            Text(
                text = "Nazwa: " +field1, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))


        }
        if(!added){
        Button(onClick = { add = true ;added=true}) {
            Text(text = "Dodaj znajomego")
        }}
        else{
            Text(text = "Dodano")
        }

    }
    LaunchedEffect(add) {
        if(add){
            AcceptInvitation(field1.toString())
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun friendsManager(navController: NavHostController){
    var found : Boolean by remember {mutableStateOf(false)}
    var databaseOutput: Int by remember { mutableStateOf(0) }
    var doneWriting : Boolean by remember {mutableStateOf(false)}
    var newUser by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var clicked : Boolean by remember {mutableStateOf(false) }
    var clickedInvitation : Boolean by remember { mutableStateOf(false) }
    var clickedAdd : Boolean by remember { mutableStateOf(false) }
    var message : String by remember { mutableStateOf("Wyszukaj znajomych") }
    val friends = remember { mutableStateListOf<Friend>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 60.dp)
    ) {
        Text(message, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField( value = newUser,
            onValueChange = { newUser = it },
            keyboardActions = KeyboardActions(onDone = {
                doneWriting=true
                focusManager.clearFocus()
            }),
            label = { Text("Wprowadz nazwe uzytkownika") },
            modifier = Modifier.padding(top = 20.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)

        )
        Button(onClick = { clicked = true}) {
                Text(text = "Sprawdz")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if(found == false) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .border(1.dp, Color.Blue)
            ) {

            }
        }
        else{
            if(databaseOutput==1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(1.dp, Color.Blue)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.person), // replace with your image resource
                        contentDescription = "User Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.White)
                    )
                    Text(text = newUser)
                    Button(onClick = {clickedInvitation = true
                    message="Wyslano zaproszenie"}) {
                        Text(text = "Dodaj")

                    }
                }
            }
            else{
                Text(text = "Nie znaleziono")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Osoby, które chcą cię dodać",modifier = Modifier.fillMaxWidth())
        Button(onClick ={ clickedAdd=true}) {
            Text("Sprawdz")
        }
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp)
        ) {
            itemsIndexed(friends) { index, friend ->
                PendingItem(friend)
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
    LaunchedEffect(clicked) {
        if (newUser != "") {
            var person = findUser(newUser)
            if (person.isEmpty()) {
                databaseOutput = 0
            } else {
                databaseOutput = 1
                if (person[0].nickname != userInformation.name)
                    newUser = person[0].nickname
            }
            found = true
            doneWriting = false
            clicked = false
        }
    }

    LaunchedEffect(clickedInvitation) {
        if(clickedInvitation!=false) {
            sendInvitation(newUser)
            clickedInvitation = false
        }
    }
    LaunchedEffect(clickedAdd) {
        if(clickedAdd==true) {
            val fetchedFriends = getInvitations()
            friends.clear()
            friends.addAll(fetchedFriends.filter { it.stage == "Pending"})
            clicked = false
        }

    }
}


@Composable
fun FriendDetail(){

}

suspend fun findUser(newUser: String): List<User> {
    val apiService = ApiClient.create()
    var person = apiService.checkFriend(newUser)
    return person
}
suspend fun sendInvitation(newUser: String){
    val apiService = ApiClient.create()
    apiService.sendFriend(userInformation.name,newUser)
}
suspend fun AcceptInvitation(user: String){
    val apiService = ApiClient.create()
    apiService.accInvitation( user, userInformation.name)
}

suspend fun getFriendRequests(user : String): List<Friend> {
    val apiService = ApiClient.create()
    return apiService.CheckFriends(userInformation.name)
}
suspend fun getFriends() : MutableList<Friend> {
    val apiService = ApiClient.create()
    return apiService.CheckFriends(userInformation.name).toMutableList()
}
suspend fun getInvitations() : MutableList<Friend> {
    val apiService = ApiClient.create()
    return apiService.getInvitation(userInformation.name).toMutableList()
}
suspend fun deleteFriend(username : String){

    val apiService = ApiClient.create()
    apiService.delFriend(userInformation.name,username)

}