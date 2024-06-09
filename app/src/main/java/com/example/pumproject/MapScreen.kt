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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun MapScreen(userLogged:String, navigationController: NavHostController,
              modifier: Modifier = Modifier
) {

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
                val a=Place(1,"WFIA","51.1165","17.0283","Tablice na zewnątrz",TypeOfPlace.PUBLIC,"Alek")
                val b=Place(1,"Miejscówka na słodowej","51.1165","17.0367","Piaścik po algorytmach:))))",TypeOfPlace.PUBLIC,"Mariusz")
                val c=Place(1,"Pierwszy zamach na Civica","51.1227","17.0421","Chło mi z kopa w drzwi wjechał",TypeOfPlace.PUBLIC,"Bonifacy123")
                val d=Place(1,"Spot na piwo","51.9721","17.8893","Koniec i bomba kto oznaczał ten tromba",TypeOfPlace.PUBLIC,"Stefan")
                var ListOfPlaces= mutableListOf<Place>(a,b,c,d);
                loadDataWithBaseURL(null,generateMap(ListOfPlaces), "text/html", "UTF-8",null)

            }
        })
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