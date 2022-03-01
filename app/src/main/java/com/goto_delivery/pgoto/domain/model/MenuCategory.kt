package com.goto_delivery.pgoto.domain.model

data class MenuCategory(
    val name: String = "",
    val food: List<Food> = listOf()
)
