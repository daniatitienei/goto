package com.goto_delivery.pgoto.domain.model

data class Cart(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)
