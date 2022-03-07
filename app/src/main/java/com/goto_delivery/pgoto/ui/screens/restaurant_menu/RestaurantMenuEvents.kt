package com.goto_delivery.pgoto.ui.screens.restaurant_menu

sealed class RestaurantMenuEvents {
    object OnPopBackStack : RestaurantMenuEvents()
    object OnFoodClick : RestaurantMenuEvents()

    data class OnSearchFood(val value: String) : RestaurantMenuEvents()
}
