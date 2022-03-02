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
                sendEvent(UiEvent.Navigate(route = event.route))
            }
            is LocationEvents.OnRequestPermission -> {
                sendEvent(UiEvent.RequestPermissionDialog)
            }
            is LocationEvents.OnTurnOnLocationDialog -> {
                sendEvent(UiEvent.AlertDialog)
            }
            is LocationEvents.OnUpdateAddress -> {
                useCases.updateAddress(event.newAddress)
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

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}