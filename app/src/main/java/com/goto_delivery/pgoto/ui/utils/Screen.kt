package com.goto_delivery.pgoto.ui.utils

sealed class Screen(val route: String) {
    object Register : Screen(route = "register")
    object Login : Screen(route = "login")

    object TurnOnLocation : Screen(route = "turn_on_location")

    object RestaurantList : Screen(route = "restaurant_list")
    object RestaurantMenu : Screen(route = "restaurant_menu/restaurant?restaurantId={restaurantId}")

    object Cart : Screen(route = "cart/cartItems?items={cartItems}&restaurantId={restaurantId}")
}
