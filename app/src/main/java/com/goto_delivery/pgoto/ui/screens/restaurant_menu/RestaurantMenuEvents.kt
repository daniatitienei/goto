package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import com.goto_delivery.pgoto.domain.model.CartItem
import com.goto_delivery.pgoto.domain.model.Food

sealed class RestaurantMenuEvents {
    object OnPopBackStack : RestaurantMenuEvents()
    data class OnAddToCartClick(val food: CartItem) : RestaurantMenuEvents()

    data class OnSearchFood(val value: String) : RestaurantMenuEvents()


    data class OnIncreaseQuantity(val food: Food) : RestaurantMenuEvents()
    data class OnDecreaseQuantity(val food: Food) : RestaurantMenuEvents()
}
