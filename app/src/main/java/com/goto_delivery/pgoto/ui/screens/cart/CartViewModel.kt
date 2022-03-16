package com.goto_delivery.pgoto.ui.screens.cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goto_delivery.pgoto.domain.model.Cart
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.ui.utils.Resource
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val moshi: Moshi,
    private val useCases: RestaurantUseCases
) : ViewModel() {

    private var _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    init {
        _state.value = _state.value.copy(isLoading = true)

        val cartJson = savedStateHandle.get<String>("cartItems")
        val restaurantIdJson = savedStateHandle.get<String>("restaurantId")
        val cartJsonAdapter = moshi.adapter(Cart::class.java)

        cartJson?.let {
            _state.value = _state.value.copy(cart = cartJsonAdapter.fromJson(it)!!)
        }

        restaurantIdJson?.let {
            val restaurantId = it.toInt()

            useCases.getRestaurantById(id = restaurantId)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _state.value = _state.value.copy(
                                restaurant = resource.data!!,
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = resource.exception?.message
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

}