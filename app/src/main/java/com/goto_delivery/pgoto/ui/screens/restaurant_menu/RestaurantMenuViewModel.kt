package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goto_delivery.pgoto.domain.model.CartItem
import com.goto_delivery.pgoto.domain.model.Food
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.ui.utils.Resource
import com.goto_delivery.pgoto.ui.utils.UiEvent
import com.goto_delivery.pgoto.ui.utils.mappers.toCartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantMenuViewModel @Inject constructor(
    private val useCases: RestaurantUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(RestaurantMenuState())
    val state: State<RestaurantMenuState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        val id = savedStateHandle.get<String>("restaurantId")

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

    fun onEvent(event: RestaurantMenuEvents) {
        when (event) {
            is RestaurantMenuEvents.OnPopBackStack -> {
                emitEvent(UiEvent.PopBackStack)
            }
            is RestaurantMenuEvents.OnAddToCartClick -> {
                val newCartItems = _state.value.cart.items + event.foodList.map { it.toCartItem() }
                _state.value = _state.value.copy(
                    cart = _state.value.cart.copy(
                        items = newCartItems,
                        total = calculateCartTotal(cart = newCartItems)
                    )
                )
            }
            is RestaurantMenuEvents.OnSearchFood -> {
                filterFood(text = event.value)
            }
            is RestaurantMenuEvents.OnIncreaseQuantity -> {
                increaseQuantity(event.food.toCartItem())
            }
            is RestaurantMenuEvents.OnDecreaseQuantity -> {
                decreaseQuantity(event.food.toCartItem())
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun increaseQuantity(cartItem: CartItem) {

        val newCart = _state.value.cart.items.map {
            if (it.name == cartItem.name)
                it.copy(quantity = it.quantity + 1)
            else it
        }
        _state.value = _state.value.copy(
            cart = _state.value.cart.copy(
                items = newCart,
                total = calculateCartTotal(newCart)
            )
        )
    }

    private fun decreaseQuantity(cartItem: CartItem) {
        val mutableCart = _state.value.cart.items.toMutableList()

        mutableCart.forEach {
            if (it.name == cartItem.name) {
                val currentQuantity = it.quantity - 1

                if (currentQuantity <= 0)
                    mutableCart.remove(it)
            }
        }

        val newCart = mutableCart.map {
            if (it.name == cartItem.name) {
                val currentQuantity = it.quantity - 1

                it.copy(quantity = currentQuantity)
            } else it
        }

        _state.value = _state.value.copy(
            cart = _state.value.cart.copy(
                items = newCart,
                total = calculateCartTotal(newCart)
            )
        )
    }

    private fun calculateCartTotal(cart: List<CartItem>): Double {
        var total = 0.0

        cart.forEach { food ->
            total += (food.price * food.quantity)
        }

        return total
    }

    private fun filterFood(text: String) {
        val filteredFood = mutableStateListOf<Food>()

        _state.value.restaurant.menu.forEach { category ->
            filteredFood.addAll(
                category.food.filter { food ->
                    food.name.contains(text, ignoreCase = true)
                }
            )
        }

        _state.value = _state.value.copy(
            filteredFoodList = filteredFood
        )
    }
}