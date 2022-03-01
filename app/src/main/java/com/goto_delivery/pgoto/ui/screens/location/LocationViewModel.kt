package com.goto_delivery.pgoto.ui.screens.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goto_delivery.pgoto.ui.utils.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: LocationEvents) {
        when (event) {
            is LocationEvents.OnNavigate -> {
                sendEvent(UiEvent.Navigate(route = event.route))
            }
            is LocationEvents.OnRequestPermission -> {
                sendEvent(UiEvent.AlertDialog)
            }
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}