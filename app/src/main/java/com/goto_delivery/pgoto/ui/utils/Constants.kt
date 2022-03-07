package com.goto_delivery.pgoto.ui.utils

import com.goto_delivery.pgoto.domain.model.Food
import com.goto_delivery.pgoto.domain.model.MenuCategory
import com.goto_delivery.pgoto.domain.model.Restaurant

object Constants {
    val food = Food(
        name = "Pizza Quatro Stagioni",
        price = 25.00,
        ingredients = "Sos de rosii, salam, ciuperci, sunca"
    )
    val restaurant = Restaurant(
        name = "Coda-Vinci",
        menu = listOf(
            MenuCategory(
                name = "Pizza",
                food = listOf(
                    Food(
                        name = "Pizza Quatro Stagioni",
                        price = 25.00,
                        ingredients = "Sos de rosii, salam, sunca, cascaval, peperroni"
                    ), Food(
                        name = "Pizza Quatro Fromaggi",
                        price = 25.00,
                        ingredients = "Sos de rosii, salam"
                    )
                )
            ),
            MenuCategory(
                name = "Shaorma",
                food = listOf(
                    Food(
                        name = "Shaorma mare",
                        price = 15.00,
                        ingredients = "Piept de pui, lipie"
                    ),
                    Food(
                        name = "Shaorma mica",
                        price = 13.00,
                        ingredients = "Piept de pui, lipie"
                    ),
                )
            )
        ),
        currency = "RON",
        rating = 4.8,
        deliveryFee = 0.0,
        estimatedDeliveryTime = "25-30 min."
    )
}