package com.goto_delivery.pgoto.domain.model

import com.google.firebase.Timestamp

data class LatestOrder(
    val shippingAddress: String = "",
    val currency: String = "",
    val deliveryFee: String = "",
    val foodList: List<Food> = emptyList(),
    val placedOn: Timestamp = Timestamp.now()
)
