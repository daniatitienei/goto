package com.goto_delivery.pgoto.domain.use_case.restaurant

import com.goto_delivery.pgoto.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetFoodCategories @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(city: String) = repository.getFoodCategories(city)
}