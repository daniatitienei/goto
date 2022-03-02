package com.goto_delivery.pgoto.ui.utils

sealed class Screens(val route: String) {
    object Register : Screens(route = "register")
    object Login : Screens(route = "login")
    object TurnOnLocation : Screens(route = "turn_on_location")
    object RestaurantList : Screens(route = "restaurant_list")
}
