package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import com.goto_delivery.pgoto.domain.model.Food
import com.goto_delivery.pgoto.domain.model.MenuCategory
import com.goto_delivery.pgoto.domain.model.Restaurant

data class RestaurantMenuState(
    val restaurant: Restaurant = Restaurant(),
    val filteredFoodList: List<Food> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
