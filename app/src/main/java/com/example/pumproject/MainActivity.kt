package com.example.pumproject

import android.annotation.SuppressLint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.pumproject.databaseConnection.ApiClient
import com.example.pumproject.ui.theme.PUMprojectTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.log

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.pumproject.ui.theme.PUMprojectTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pumproject.ui.theme.Pink40
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PUMprojectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }
}

internal enum class State {
    Login, Register, Map, Profile, Places
}


@Composable
fun Main(modifier: Modifier = Modifier) {

    var currentState by remember { mutableStateOf(State.Login) }
    var userLogged by remember { mutableStateOf("") }

    Surface(modifier) {
        if (currentState==State.Login) {
            LoginScreen(
                onButtonClicked = { login, state ->
                    userLogged=login
                    currentState = state // Navigate to the map screen
                })
        } else if (currentState==State.Map) {
            MyBottomApp(userLogged)
        }
        else if (currentState==State.Register) {
            RegisterScreen(onButtonClicked = { newState -> currentState = newState as State })
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onButtonClicked: (Any?) -> Unit,
    modifier: Modifier = Modifier
) {
    var login by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var databaseFeedback by remember { mutableStateOf("") }
    var buttonClicked by remember { mutableStateOf(false) }
    var loginReturnCode by remember { mutableStateOf(-1) }
    var doneLogin by remember { mutableStateOf(false) }
    var donePassword by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(

        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="Register",
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 40.dp))

        Text(text=databaseFeedback,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 20.dp),
            color = Color.Red)

        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            keyboardActions = KeyboardActions(onDone = {
                    doneLogin=true
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            keyboardActions = KeyboardActions(onDone = {
                donePassword=true
            }),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(top = 20.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)
        )

        Button(
            modifier = Modifier
                .padding(top = 24.dp),
            onClick = {
            coroutineScope.launch {
                loginReturnCode = Database_register_info(login.text,password.text)
                if (loginReturnCode==0)
                { databaseFeedback="Registered successfully!"
                }
                else if(loginReturnCode==1)
                    databaseFeedback="Other user use this login"
                if(login.text.length<5)
                    databaseFeedback="Incorect login, minimum length is five letters"
                else if(password.text.length<5)
                    databaseFeedback="Incorect password, minimum length is five letters"

            }}){
            Text("Register")
        }

        Button(
            modifier = Modifier
                .padding(vertical = 24.dp),
            onClick = {
                onButtonClicked(State.Login)
            }) {
            Text("Login")
        }


    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = loginReturnCode.toString())
        Text(text = login.text)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    onButtonClicked: (String, State) -> Unit,
    modifier: Modifier = Modifier
)
{
    var login by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var database_feedback by remember { mutableStateOf("") }
    var buttonClicked = remember { mutableStateOf(false) }
    var loginReturnCode by remember {
        mutableStateOf(-1)
    }





    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="Login",
             fontSize = 40.sp,
            modifier = Modifier.padding(top = 40.dp))

        Text(text=database_feedback,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 20.dp),
            color = Color.Red)

        TextField(
            value = login,
            onValueChange = {
                login = it
                },
            label = { Text("Login") },
                    keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done)
        )
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

            Button(
                modifier = Modifier
                    .padding(top = 24.dp),
                onClick = {
                    buttonClicked.value = true
                    if (loginReturnCode==2 && (password.text.length>4) && (login.text.length>4))
                        { onButtonClicked(login.text, State.Map)
                    }
                    else if(loginReturnCode==1)
                        database_feedback="Wrong password"
                    else if(loginReturnCode==0)
                        database_feedback="User does not exist"
                    if(password.text.toString().length<5 || login.text.toString().length<5)
                    {
                        database_feedback="Password or login to short"
                    }

                })

            {
                Text("Login")
            }

            Button(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                onClick = {
                    onButtonClicked(null.toString(),State.Register)
                }) {
                Text("Create account")
            }


    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = loginReturnCode.toString())
        Text(text = login.text)
    }
    LaunchedEffect(login,password) {
        loginReturnCode = Database_info(login.text, password.text)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomApp(userLogged:String) {
    val navigationController = rememberNavController()
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF85C1E9 )
            ) {
                IconButton(onClick = {
                    selected.value = Icons.Default.Home
                    navigationController.navigate(Screens.MapScreen.screen) { popUpTo(0) }
                }, modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                    )
                }

                IconButton(onClick = {
                    selected.value = Icons.Default.Menu
                    navigationController.navigate(Screens.PlacesScreen.screen) { popUpTo(0) }
                }, modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Menu) Color.White else Color.DarkGray
                    )
                }

                IconButton(onClick = {
                    selected.value = Icons.Default.Person
                    navigationController.navigate(Screens.ProfileScreen.screen) { popUpTo(0) }
                }, modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Person) Color.White else Color.DarkGray
                    )
                }
            }
        }
    ) {
        paddingValues ->
        NavHost(navigationController, startDestination = Screens.MapScreen.screen) {
            composable(Screens.MapScreen.screen) {
                MapScreen(userLogged = userLogged)
            }
            composable(Screens.PlacesScreen.screen) {
                PlacesScreen()
            }
            composable(Screens.ProfileScreen.screen) {
                ProfileScreen()
            }
        }
    }
}

suspend fun Database_info(name: String=" ", pass: String=" "): Int
{

     val apiService = ApiClient.create()
     val user = apiService.getUser(name.toString(),pass.toString())
    if(user.isEmpty())
        return 0

    if(user[0].nickname==name&&user[0].hashPassword!=pass)
        return 1
    if(user[0].nickname==name&&user[0].hashPassword==pass)
        return 2
    return 3   //default
}

suspend fun Database_register_info(name: String=" ", pass: String=" "): Int
{
    val apiService = ApiClient.create()
    if(name.length>5 && pass.length > 5) {
        val output = apiService.AddUser(name.toString(), pass.toString())
        println(output)
        return output.output
    }


    return 2     //0 dodano pomyslnie / 1 - istnieje //2 za krotkie
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PUMprojectTheme {
        MyBottomApp("place_holder")
    }
}