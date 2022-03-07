package com.goto_delivery.pgoto.ui.screens.login

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.domain.use_case.authentication.AuthenticationUseCases
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
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val useCases: AuthenticationUseCases
) : ViewModel() {
    private var _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private val _passwordError = mutableStateOf<String?>(null)
    val passwordError: State<String?> = _passwordError

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.OnValidate -> {
                _passwordError.value = null
                _emailError.value = null

                if (event.password.isEmpty()) {
                    _passwordError.value = application.getString(R.string.field_must_be_completed)
                    return
                } else if (event.email.isEmpty()) {
                    _emailError.value = application.getString(R.string.field_must_be_completed)
                    return
                } else if (!Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) {
                    _emailError.value = application.getString(R.string.error_invalid_email)
                    return
                }

                useCases.login(email = event.email, password = event.password)
                    .onEach { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                /* TODO */
                            }
                            is Resource.Loading -> {

                            }
                            is Resource.Error -> {
                                when (resource.exception) {
                                    is FirebaseAuthInvalidUserException -> {
                                        _emailError.value =
                                            application.getString(R.string.error_user_does_not_exist)
                                    }
                                    is FirebaseAuthInvalidCredentialsException -> {
                                        _passwordError.value =
                                            application.getString(R.string.error_wrong_password)
                                    }
                                }
                            }
                        }
                    }.launchIn(viewModelScope)
            }
            is LoginEvents.OnNavigate -> {
                emitEvent(
                    UiEvent.Navigate(
                        route = event.route,
                        popUpTo = event.popUpTo
                    )
                )
            }
            is LoginEvents.OnContinueWithFacebook -> {
                /* TODO */
            }
            is LoginEvents.OnContinueWithGoogle -> {
                if (event.account != null) {
                    useCases.continueWithGoogle(event.account.idToken!!)
                        .onEach { resource ->
                            when (resource) {
                                is Resource.Success -> {
                                    /* TODO */
                                }
                                is Resource.Loading -> {
                                    /* TODO */
                                }
                                is Resource.Error -> {
                                    /* TODO */
                                }
                            }
                        }.launchIn(viewModelScope)
                } else {
                    emitEvent(UiEvent.Toast(message = application.getString(R.string.google_auth_failed)))
                }
            }
        }
    }

    private fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}