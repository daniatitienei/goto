package com.goto_deliveryl.pgoto.ui.utils

sealed class UiEvent {
    data class Navigate(val route: String, val popUpTo: PopUpTo? = null) : UiEvent() {
        data class PopUpTo(
            val route: String,
            val inclusive: Boolean
        )
    }

    object PopBackStack : UiEvent()

    data class Toast(val message: String) : UiEvent()
}