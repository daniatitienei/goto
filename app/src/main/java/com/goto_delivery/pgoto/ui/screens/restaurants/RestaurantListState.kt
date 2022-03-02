package com.goto_delivery.pgoto.ui.screens.restaurants

import com.goto_delivery.pgoto.domain.model.Restaurant

data class RestaurantListState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
