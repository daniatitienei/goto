package com.goto_delivery.pgoto.domain.model

data class Restaurant(
    val id: Int = 0,
    val name: String = "",
    val estimatedDeliveryTime: String = "",
    val imageUrl: String = "",
    val city: String = "",
    val rating: Double = 0.0,
    val currency: String = "",
    val deliveryFee: Double = 0.0,
    val categories: List<String> = emptyList(),
    val menu: List<MenuCategory> = emptyList(),
    val isOpen: Boolean = false,
    val packFee: Double = 0.0
)
