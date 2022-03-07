package com.goto_delivery.pgoto.ui.screens.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class LocationViewModel @Inject constructor(
    private val useCases: AccountUseCases
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: LocationEvents) {
        when (event) {
            is LocationEvents.OnNavigate -> {
                emitEvent(UiEvent.Navigate(route = event.route, popUpTo = event.popUpTo))
            }
            is LocationEvents.OnRequestPermission -> {
                emitEvent(UiEvent.RequestPermissionDialog)
            }
            is LocationEvents.OnTurnOnLocationDialog -> {
                emitEvent(UiEvent.AlertDialog)
            }
            is LocationEvents.OnUpdateAddress -> {
                useCases.updateAddress(event.newAddress, event.city)
                    .onEach { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                Log.d("address", "updated")
                            }
                            is Resource.Error -> {
                                Log.d("address", "failed")
                            }
                            else -> Unit
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}