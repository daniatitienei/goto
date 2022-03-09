package com.goto_delivery.pgoto.domain.model

data class Food(
    val name: String = "",
    val ingredients: String = "",
    val price: Double = 0.0,
    val imageUrl: String? = null,
    val suggestions: List<Food> = emptyList()
)
