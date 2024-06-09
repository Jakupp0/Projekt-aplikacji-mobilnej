package com.example.pumproject

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.pumproject.databaseConnection.ApiClient
import com.example.pumproject.Place
import com.example.pumproject.databaseConnection.Friend
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun MapScreen(userLogged:String, navigationController: NavHostController,
              modifier: Modifier = Modifier
) {
    var places = remember { mutableStateListOf<com.example.pumproject.databaseConnection.Place>() }
    var datataken by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
          var  gotplace = getPlacesforMap()
        places.addAll(gotplace)
        datataken = true


    }
    if(datataken) {
        Surface(modifier = modifier.padding(0.dp)) {
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
                    //dummy data

                    var ListOfPlaces = mutableListOf<Place>()
                    for (x in places) {
                        var newPlace = Place(
                            1, x.Name, x.Latitude, x.Longitude, x.Description, TypeOfPlace.PUBLIC,
                                x.UserName
                        )

                        ListOfPlaces.add(newPlace)


                    }



                    loadDataWithBaseURL(null, generateMap(ListOfPlaces), "text/html", "UTF-8", null)

                }
            })

        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 100.dp), // Adjusted padding to provide more space from the bottom
        contentAlignment = Alignment.BottomEnd // Align to the bottom start (left) of the container
    ) {
        Button(
            onClick = {  navigationController.navigate(Screens.AddPlaceScreen.screen) { popUpTo(0) } },
            shape = CircleShape,
            modifier = Modifier
                .size(100.dp)
                .padding(0.dp)
        ) {
            Text(
                text = "+",
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}




fun generateMap(ListOfPlaces: MutableList<Place>): String
{
    val mapCenter = Pair(51.10, 17.040)
//"L.marker([51.1165,17,0283]).addTo(map).bindPopup('A pretty CSS popup.<br> Easily customizable.');\n";
    var popupString="";


    ListOfPlaces.forEach { place ->
       popupString+="L.marker([${place.Latitude}, ${place.Longitude}]).addTo(map).bindPopup('<b>${place.Name}</b><br> ${place.Description} <br>Dodane przez: ${place.UserName}');\n";
    }


    // Generate HTML content
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
            "        var map = L.map('map').setView(["+mapCenter.first+", "+mapCenter.second+"], 13);\n" +
            "        \n" +
            "        // Add tile layer (OpenStreetMap)\n" +
            "        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
            "            maxZoom: 19,\n" +
            "        }).addTo(map);\n" +
            popupString+
            "    </script>\n" +
            "</body>\n" +
            "</html>"
    return htmlContent

}

suspend fun getPlacesforMap(): MutableList<com.example.pumproject.databaseConnection.Place>{
    val apiService = ApiClient.create()
    var friends = apiService.CheckFriends(userInformation.name)
    var names : MutableList<String> = mutableListOf()
    for (x in friends){
        names.add(x.username1)
        names.add(x.username2)
    }
    names.removeIf { it == userInformation.name }
    var places : MutableList<com.example.pumproject.databaseConnection.Place> = apiService.getAllplaces()
    places.removeIf { it.Type == com.example.pumproject.databaseConnection.TypeOfPlace.PRIVATE && it.UserName!= userInformation.name }

    return places
}