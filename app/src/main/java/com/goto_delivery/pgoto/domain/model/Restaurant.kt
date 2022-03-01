package com.goto_delivery.pgoto.domain.model

data class Restaurant(
    val name: String = "",
    val estimatedDeliveryTime: String = "",
    val imageUrl: String = "",
    val city: String = "",
    val rating: Double = 0.0,
    val currency: String = "",
    val deliveryFee: Double = 0.0,
    val menu: List<MenuCategory> = emptyList()
)
