package com.goto_delivery.pgoto.ui.screens.restaurants

import com.goto_delivery.pgoto.domain.model.Restaurant
import com.goto_delivery.pgoto.domain.model.User

data class RestaurantListState(
    val restaurants: List<Restaurant> = emptyList(),
    val foodCategories: List<String> = emptyList(),
    val account: User = User(),
    val isLoading: Boolean = false,
    val error: String? = null
)
