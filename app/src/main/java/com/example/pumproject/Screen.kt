package com.example.pumproject

sealed class Screens(val screen: String)
{
    object MapScreen: Screens("map")
    object PlacesScreen: Screens("places")
    object ProfileScreen: Screens("profile")
}
