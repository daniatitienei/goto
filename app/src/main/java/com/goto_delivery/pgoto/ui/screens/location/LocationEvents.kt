package com.goto_delivery.pgoto.ui.screens.location

import com.goto_delivery.pgoto.ui.utils.UiEvent

sealed class LocationEvents {
    data class OnNavigate(val route: String, val popUpTo: UiEvent.Navigate.PopUpTo? = null) : LocationEvents()

    object NavigateToRestaurantList : LocationEvents()
    object NavigateToSelectAddressScreen : LocationEvents()

    object OnRequestPermission : LocationEvents()

    data class OnUpdateAddress(val newAddress: String, val city: String) : LocationEvents()

    object OnTurnOnLocationDialog : LocationEvents()
}
