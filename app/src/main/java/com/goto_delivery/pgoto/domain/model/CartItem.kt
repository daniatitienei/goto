package com.goto_delivery.pgoto.domain.model

data class CartItem(
    val name: String = "",
    val ingredients: String = "",
    val price: Double = 0.0,
    val imageUrl: String? = null,
    val quantity: Int = 1,
    val suggestions: List<Food> = emptyList()
)
