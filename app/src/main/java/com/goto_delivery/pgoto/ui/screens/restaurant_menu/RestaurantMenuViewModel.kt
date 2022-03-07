package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.ui.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RestaurantMenuViewModel @Inject constructor(
    private val useCases: RestaurantUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(RestaurantMenuState())
    val state: State<RestaurantMenuState> = _state

    init {
        val id = savedStateHandle.get<String>("restaurantId")

        Log.d("restaurant_menu_id", id.toString())

        useCases.getRestaurantById(id?.toInt()!!)
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            restaurant = resource.data!!,
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
                            error = resource.exception?.localizedMessage
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }
}