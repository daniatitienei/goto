package com.goto_delivery.pgoto.ui.utils.mappers

import com.goto_delivery.pgoto.domain.model.CartItem
import com.goto_delivery.pgoto.domain.model.Food

fun Food.toCartItem() =
    CartItem(
        name, ingredients, price,
    )