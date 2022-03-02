package com.goto_delivery.pgoto.ui.screens.location

sealed class LocationEvents {
    data class OnNavigate(val route: String) : LocationEvents()

    object OnRequestPermission : LocationEvents()

    data class OnUpdateAddress(val newAddress: String) : LocationEvents()

    object OnTurnOnLocationDialog : LocationEvents()
}
