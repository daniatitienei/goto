package com.goto_delivery.pgoto.ui.screens.restaurants

sealed class RestaurantListEvents {
    object OnNavigate : RestaurantListEvents()
    data class OnSelectCategory(val foodCategory: String) : RestaurantListEvents()
}
