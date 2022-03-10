package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import com.goto_delivery.pgoto.domain.model.Food

sealed class RestaurantMenuEvents {
    object OnPopBackStack : RestaurantMenuEvents()
    data class OnAddToCartClick(val foodList: List<Food>) : RestaurantMenuEvents()

    data class OnSearchFood(val value: String) : RestaurantMenuEvents()
}
