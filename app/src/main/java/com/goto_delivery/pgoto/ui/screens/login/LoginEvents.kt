package com.goto_delivery.pgoto.ui.screens.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.goto_delivery.pgoto.ui.utils.UiEvent

sealed class LoginEvents {
    data class OnValidate(
        val email: String,
        val password: String,
    ) : LoginEvents()

    data class OnNavigate(
        val route: String,
        val popUpTo: UiEvent.Navigate.PopUpTo? = null
    ) : LoginEvents()

    data class OnContinueWithGoogle(val account: GoogleSignInAccount?) : LoginEvents()
    object OnContinueWithFacebook : LoginEvents()
}