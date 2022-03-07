package com.goto_delivery.pgoto.ui.screens.restaurants

sealed class RestaurantListEvents {
    data class OnNavigate(val route: String) : RestaurantListEvents()

    data class OnFilterRestaurantBySearch(val text: String) : RestaurantListEvents()
    object OnClearRestaurantFilter : RestaurantListEvents()
    data class OnFilterByCategory(val category: String) : RestaurantListEvents()
}
