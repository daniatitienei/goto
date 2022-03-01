package com.goto_delivery.pgoto.domain.model

data class User(
    val email: String = "",
    val fullName: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val latestOrders: List<LatestOrder> = emptyList(),
)
