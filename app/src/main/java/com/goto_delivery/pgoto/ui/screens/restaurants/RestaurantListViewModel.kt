package com.goto_delivery.pgoto.ui.screens.restaurants

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goto_delivery.pgoto.domain.use_case.restaurant.RestaurantUseCases
import com.goto_delivery.pgoto.domain.use_case.user.AccountUseCases
import com.goto_delivery.pgoto.ui.utils.Resource
import com.goto_delivery.pgoto.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val accountUseCases: AccountUseCases
) : ViewModel() {

    private val _state = mutableStateOf(RestaurantListState())
    val state: State<RestaurantListState> = _state

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getAccount()
    }

    fun onEvent(event: RestaurantListEvents) {
        when (event) {
            is RestaurantListEvents.OnNavigate -> {

            }
            is RestaurantListEvents.OnSelectCategory -> {

            }
            is RestaurantListEvents.OnFilterByCategory -> {
                _state.value = _state.value.copy(
                    filteredResults = _state.value.restaurants
                        .filter { restaurant ->
                            restaurant.categories.contains(event.category)
                        }
                )
            }
            is RestaurantListEvents.OnClearFilter -> {
                _state.value = _state.value.copy(
                    filteredResults = emptyList()
                )
            }
            is RestaurantListEvents.OnFilterBySearch -> {
                _state.value = _state.value.copy(
                    filteredResults = _state.value.restaurants
                        .filter {
                            it.name.contains(event.text, ignoreCase = true)
                        }
                )
            }
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun getRestaurants() {
        restaurantUseCases.getRestaurants(_state.value.account.city)
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            restaurants = resource.data!!,
                            isLoading = false
                        )

                        getCategories()
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

    private fun getAccount() {
        accountUseCases.getAccountInfo().onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        account = resource.data!!,
                    )

                    getRestaurants()
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

    private fun getCategories() {
        restaurantUseCases.getFoodCategories(city = _state.value.account.city)
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            foodCategories = resource.data!!,
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