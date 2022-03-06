package com.goto_delivery.pgoto.ui.screens.restaurants

sealed class RestaurantListEvents {
    object OnNavigate : RestaurantListEvents()

    data class OnFilterRestaurantBySearch(val text: String) : RestaurantListEvents()
    object OnClearRestaurantFilter : RestaurantListEvents()
    data class OnFilterByCategory(val category: String) : RestaurantListEvents()
}
