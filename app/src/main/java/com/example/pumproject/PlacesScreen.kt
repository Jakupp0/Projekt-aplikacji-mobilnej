package com.example.pumproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pumproject.databaseConnection.Friend
import com.example.pumproject.databaseConnection.Place
import com.example.pumproject.databaseConnection.TypeOfPlace

@Composable
fun PlacesScreen(modifier: Modifier = Modifier) {
    Surface(modifier) {
        Text(text="PlacesScreen",
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 40.dp))
    }
}

@Composable
fun PlaceItemUser(item: Place) {
    var placeName by remember { mutableStateOf(item.Name) }
    var creationDate by remember { mutableStateOf(item.CreatedAt) }
    var delete : Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, Color.Blue)

    ) {
        Row() {
            Image(
                painter = painterResource(id = R.drawable.baseline_fmd_good_24), // replace with your image resource
                contentDescription = "Place Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White).clickable {  }
            )

            Text(
                text = "Nazwa: " +placeName, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))


        }
        Row(){
            Text(
                text = "Dodano: "+creationDate, modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { delete = true ; }) {
                Text(text = "Usuń miejsce")
            }
            if(item.Type == TypeOfPlace.PUBLIC) {
                Image(
                    painter = painterResource(id = R.drawable.placepublic), // replace with your image resource
                    contentDescription = "Place Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White).clickable { }
                )
            }
            else{
                Image(
                    painter = painterResource(id = R.drawable.placeprivate), // replace with your image resource
                    contentDescription = "Place Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White).clickable { }
                )
            }
            }




    }
    LaunchedEffect(delete) {
        if(delete==true){
            deletePlace(item)
        }
    }
}

suspend fun deletePlace(place: Place){

}