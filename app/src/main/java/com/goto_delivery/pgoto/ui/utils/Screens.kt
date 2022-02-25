package com.goto_delivery.pgoto.ui.utils

sealed class Screens(val route: String) {
    object Register : Screens(route = "register")
    object Login : Screens(route = "login")
}
