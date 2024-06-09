package com.example.pumproject


import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.pumproject.databaseConnection.ApiClient
import com.example.pumproject.databaseConnection.Place
import com.example.pumproject.databaseConnection.State

import kotlinx.coroutines.launch

class JavaScriptInterface(private val updateCoordinates: (Double, Double) -> Unit) {
    @JavascriptInterface
    fun onMapClick(lat: Double, lng: Double) {
        updateCoordinates(lat, lng)
    }
}


@SuppressLint("JavascriptInterface")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceScreen(userLogged:String, navigationController: NavHostController,
                   modifier: Modifier = Modifier) {
    var Name by remember { mutableStateOf(TextFieldValue()) }
    var Description by remember { mutableStateOf(TextFieldValue()) }
    var userName by remember { mutableStateOf(TextFieldValue()) }
    var Latitude by remember { mutableStateOf(0.0) }
    var Longitude by remember { mutableStateOf(0.0) }
    var type by remember { mutableStateOf(TypeOfPlace.PUBLIC) }
    var clicked : Boolean by remember {
        mutableStateOf(false)
    }

    val mapCenter = Pair(51.10, 17.040)
    val context = LocalContext.current

    val updateCoordinates: (Double, Double) -> Unit = { lat, lng ->
        Latitude = lat
        Longitude = lng
    }
    val javaScriptInterface = remember { JavaScriptInterface(updateCoordinates) }

    val htmlContent = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet/dist/leaflet.css\" />\n" +
            "    <script src=\"https://unpkg.com/leaflet/dist/leaflet.js\"></script>\n" +
            "    <style>\n" +
            "        /* Set map container size */\n" +
            "        #map { height: 100vh; width: 100vw; margin-left: 0px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div id=\"map\"></div>\n" +
            "    <script>\n" +
            "        // Initialize map\n" +
            "        var map = L.map('map').setView([" + mapCenter.first + ", " + mapCenter.second + "], 13);\n" +
            "var currentMarker;\n" +
            "        \n" +
            "        // Add tile layer (OpenStreetMap)\n" +
            "        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
            "            maxZoom: 19,\n" +
            "        }).addTo(map);\n" +
            "map.on('click', function(e) {\n" +
            "                var lat = e.latlng.lat;\n" +
            "                var lng = e.latlng.lng;\n" +
            "                if (currentMarker) {\n" +
            "                    map.removeLayer(currentMarker);\n" +
            "                }\n" +
            "                currentMarker = L.marker([lat, lng]).addTo(map).bindPopup();\n" +
            "                Android.onMapClick(lat, lng);\n" +
            "            });\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>".trimIndent()



    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        TextField(
            value = Name,
            onValueChange = {
                Name = it
            },
            modifier = Modifier
                .width(320.dp)
                .padding(top = 10.dp),
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        TextField(
            value = Description,
            onValueChange = { Description = it },
            modifier = Modifier
                .padding(top = 10.dp)
                .height(100.dp)
                .width(320.dp),// Adjust the height to approximately match two lines of text
            label = { Text("Description") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            visualTransformation = VisualTransformation.None  // Remove password transformation
        )

        Surface(modifier = modifier
            .padding(top = 10.dp)
            .height(250.dp)) {
            AndroidView(factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        cacheMode = WebSettings.LOAD_DEFAULT
                    }
                    addJavascriptInterface(javaScriptInterface, "Android")

                    loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }

            })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = type == TypeOfPlace.PRIVATE,
                onCheckedChange = { isChecked ->
                    type = if (isChecked) TypeOfPlace.PRIVATE else TypeOfPlace.PUBLIC
                },
                modifier = Modifier.padding(8.dp)
            )


            Text(text = if (type == TypeOfPlace.PRIVATE) "Private" else "Public")
        }



        Button(
            modifier = Modifier
                .padding(vertical = 8.dp),
            onClick = {
                Place(
                    1,
                    Name.toString(),
                    Description.toString(),
                    Latitude.toString(),
                    Longitude.toString(),
                    type,
                    userLogged
                )
                navigationController.navigate(Screens.MapScreen.screen) { popUpTo(0) }

                Toast.makeText(context, "Place added", Toast.LENGTH_SHORT).show()
                clicked = true;
            }) {
            Text("Add")
        }
    }
    LaunchedEffect(clicked) {
    if(clicked == true) {
        addPlace(Name.text, Description.text, Latitude, Longitude, type.toString())
        clicked = false
    }
    }

}
suspend fun addPlace(name:String,description : String,latitude : Double,longitude : Double,type : String){

    val apiService = ApiClient.create()
    apiService.addPlace(name,latitude.toString(),longitude.toString(),description,type, userInformation.name)
}

