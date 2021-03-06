package com.goto_delivery.pgoto.domain.repository

import com.goto_delivery.pgoto.domain.model.Restaurant
import com.goto_delivery.pgoto.ui.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun getRestaurants(city: String): Flow<Resource<List<Restaurant>>>

    fun getRestaurantById(id: Int): Flow<Resource<Restaurant>>

    fun getFoodCategories(city: String): Flow<Resource<List<String>>>
}