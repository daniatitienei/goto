package com.goto_delivery.pgoto.ui.screens.restaurants

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val useCases: RestaurantUseCases
) : ViewModel() {

    private val _state = mutableStateOf(RestaurantListState())
    val state: State<RestaurantListState> = _state

    init {
        useCases.getRestaurants().onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        restaurants = resource.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = resource.exception?.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}