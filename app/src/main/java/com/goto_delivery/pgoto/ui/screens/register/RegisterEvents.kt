package com.goto_delivery.pgoto.ui.screens.register

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.goto_delivery.pgoto.ui.utils.UiEvent

sealed class RegisterEvents {
    data class OnValidate(
        val email: String,
        val name: String,
        val password: String,
    ) : RegisterEvents()

    data class OnNavigate(
        val route: String,
        val popUpTo: UiEvent.Navigate.PopUpTo? = null
    ) : RegisterEvents()

    data class OnContinueWithGoogle(val account: GoogleSignInAccount?) : RegisterEvents()
    object OnContinueWithFacebook : RegisterEvents()
}
