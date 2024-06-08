package com.example.pumproject

import android.content.Context
import android.content.Intent
import android.os.SystemClock.sleep
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.pumproject.databaseConnection.State
import com.example.pumproject.databaseConnection.UserInfo


@Composable
fun ProfileScreen(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile") {
        composable("profile") { ProfileScreen(navController) }
        composable("addFriendScreen") { AddFriendScreen(navController) }
        composable("friendListScreen") { FriendListScreen(navController) }
        composable("deleteScreen") { deleteScreen(navController,context) }
        composable("manageAccountScreen") { ManageAccountScreen(navController,context) }
        composable("changePasswordScreen") { changePasswordScreen(navController,context) }


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
                    text = "Nazwa Użytkownika",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Placeholder for RecyclerView
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.LightGray)
            ) {
                Text(
                    text = "RecyclerView Placeholder",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons for navigation
            Button(
                onClick = { navController.navigate("addFriendScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj znajomych")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("friendListScreen") },
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
fun FriendListScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Lista znajomych", fontSize = 24.sp)
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
    var deleted by remember { mutableStateOf(0) }
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