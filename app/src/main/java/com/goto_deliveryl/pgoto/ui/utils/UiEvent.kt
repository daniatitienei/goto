package com.goto_deliveryl.pgoto.ui.utils

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    object PopBackStack : UiEvent()

    data class Toast(val message: String) : UiEvent()
}
