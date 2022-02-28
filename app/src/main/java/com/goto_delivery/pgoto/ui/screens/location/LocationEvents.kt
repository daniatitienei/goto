package com.goto_delivery.pgoto.ui.screens.location

sealed class LocationEvents {
    data class OnNavigate(val route: String) : LocationEvents()

    object OnRequestPermission : LocationEvents()
}
