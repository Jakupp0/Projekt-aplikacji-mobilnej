package com.example.pumproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pumproject.databaseConnection.ApiClient
import com.example.pumproject.databaseConnection.Friend

import com.example.pumproject.databaseConnection.TypeOfPlace
import com.example.pumproject.databaseConnection.Place
@Composable
fun PlacesScreen(modifier: Modifier = Modifier) {
    val places = remember { mutableStateListOf<Place>() }
    Surface(modifier) {

        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(1.dp)
        ) {
            itemsIndexed(places) { index, place ->
                PlaceItemUser(place)
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
    LaunchedEffect(Unit) {
        val fetchedPlaces = getPlaces()
        places.clear()
        places.addAll(fetchedPlaces)


    }
}

@Composable
fun PlaceItemUser(item: Place) {
    var placeName by remember { mutableStateOf(item.Name) }
    var creationDate by remember { mutableStateOf(item.CreatedAt) }
    var delete : Boolean by remember { mutableStateOf(false) }
    var changeVisi : Boolean by remember { mutableStateOf(false) }
    var changed : Int  by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, Color.Blue)
            .height(150.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_fmd_good_24), // replace with your image resource
                contentDescription = "Place Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White)
                    .padding(10.dp)
                    .clickable { }
            )

            Spacer(modifier = Modifier.width(10.dp))

            if (!delete) {
                Text(
                    text = "Nazwa: $placeName",
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = "Usunięto",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dodano: $creationDate"
            )
            Button(
                onClick = { delete = true }
            ) {
                Text(text = "Usuń miejsce")
            }

            val imageResource = if (item.Type == TypeOfPlace.PUBLIC) {

                Image(
                    painter = painterResource(id = R.drawable.placepublic),
                    contentDescription = "Place Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White)
                        .clickable { changeVisi = true }
                )
                changed++;
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeprivate),
                    contentDescription = "Place Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White)
                        .clickable { changeVisi = true }
                )
                changed++;
            }

        }
    }
    LaunchedEffect(delete) {
        if(delete==true){
            deletePlace(item)
            delete = false
        }
    }
    LaunchedEffect(changeVisi) {
        if(changeVisi==true){
            changeVisibility(item,item.Type)

        }
    }
}





suspend fun deletePlace(place: Place){
    val apiService = ApiClient.create()
    apiService.deletePlace(place.PlaceId.toString())
}
suspend fun getPlaces() : MutableList<com.example.pumproject.databaseConnection.Place> {
    val apiService = ApiClient.create()
    val value : MutableList<com.example.pumproject.databaseConnection.Place> = apiService.getPlaces(userInformation.name).toMutableList()
    return value
}

suspend fun changeVisibility(item: Place,type :TypeOfPlace)  {
    var newType = String()
    if(type == TypeOfPlace.PUBLIC)
        newType = "PRIVATE"
    if(type == TypeOfPlace.PRIVATE)
        newType = "PUBLIC"
    val apiService = ApiClient.create()
    apiService.changeVisibility(item.PlaceId.toString(),newType)

}