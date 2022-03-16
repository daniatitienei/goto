package com.goto_delivery.pgoto.ui.screens.cart

import com.goto_delivery.pgoto.domain.model.Cart
import com.goto_delivery.pgoto.domain.model.Restaurant

data class CartState(
    val cart: Cart = Cart(),
    val total: Double = 0.0,
    val restaurant: Restaurant = Restaurant(),
    val isLoading: Boolean = false,
    val error: String? = null
)
