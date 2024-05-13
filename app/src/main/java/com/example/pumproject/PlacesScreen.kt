package com.example.pumproject

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlacesScreen(modifier: Modifier = Modifier) {
    Surface(modifier) {
        Text(text="PlacesScreen",
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 40.dp))
    }
}