package com.example.pumproject
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pumproject.databaseConnection.ApiClient
import com.example.pumproject.ui.theme.PUMprojectTheme
import kotlin.math.log


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
            MainScreen(userLogged)
        }
        else if (currentState==State.Register) {
            RegisterScreen(onButtonClicked = { newState -> currentState = newState as State })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(userLogged:String,
    modifier: Modifier = Modifier
) {
    Surface(modifier) {
        Text(text=userLogged,
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 40.dp))
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
    var loginReturnCode by remember {
        mutableStateOf(-1)
    }

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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
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

                val message="Registered succesfuly";
                if (loginReturnCode==1)
                { onButtonClicked(State.Map)
                }
                else if(loginReturnCode==0)
                    databaseFeedback="Other user use this login"
                if(login.text.length<5)
                    databaseFeedback="Incorect login, minimum length is five letters"
                else if(password.text.length<5)
                    databaseFeedback="Incorect password, minimum length is five letters"

            }){
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
    LaunchedEffect(buttonClicked) {
        loginReturnCode = Database_info(login.text, password.text)
        buttonClicked = false
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
            onValueChange = { login = it },
            label = { Text("Login") },
                    keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
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
    LaunchedEffect(buttonClicked.value) {

        loginReturnCode = Database_info(login.text, password.text)
        buttonClicked.value = false
    }
}



suspend fun Database_info(name: String=" ", pass: String=" "): Int
{
    println(name)
    val apiService = ApiClient.create()
       val user = apiService.getUser(name.toString(),pass.toString())
    if(user.isEmpty())
        return 0

    if(user[0].nickname==name&&user[0].hashPassword!=pass)
        return 1
    if(user[0].nickname==name&&user[0].hashPassword==pass)
        return 2
    return 20   //istnieje haslo ale nie ma uzytkownika
}

fun Database_register_info(): Int
{

    return 1
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PUMprojectTheme {
        Main()
    }
}