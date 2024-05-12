package com.example.pumproject

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Composable
fun MapScreen(userLogged:String,
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


                loadDataWithBaseURL(null,generateMap(), "text/html", "UTF-8",null)

            }
        })
    }
}

fun generateMap(): String
{
    val mapCenter = Pair(51.10, 17.040)

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
            "    </script>\n" +
            "</body>\n" +
            "</html>"
    return htmlContent

}