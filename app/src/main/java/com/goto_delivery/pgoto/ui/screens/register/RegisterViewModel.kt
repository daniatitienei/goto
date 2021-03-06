package com.goto_delivery.pgoto.ui.screens.register

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.domain.use_case.authentication.AuthenticationUseCases
import com.goto_delivery.pgoto.ui.utils.Resource
import com.goto_delivery.pgoto.ui.utils.NavigationGraphs
import com.goto_delivery.pgoto.ui.utils.Screen
import com.goto_delivery.pgoto.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val application: Application,
    private val useCases: AuthenticationUseCases
) : ViewModel() {
    private var _emailError = mutableStateOf<String?>(null)
    val emailError: State<String?> = _emailError

    private val _passwordError = mutableStateOf<String?>(null)
    val passwordError: State<String?> = _passwordError

    private val _nameError = mutableStateOf<String?>(null)
    val nameError: State<String?> = _nameError

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: RegisterEvents) {
        when (event) {
            is RegisterEvents.OnValidate -> {
                _passwordError.value = null
                _nameError.value = null
                _emailError.value = null

                if (event.password.isEmpty()) {
                    _passwordError.value = application.getString(R.string.field_must_be_completed)
                    return
                } else if (event.email.isEmpty()) {
                    _emailError.value = application.getString(R.string.field_must_be_completed)
                    return
                } else if (event.name.isEmpty()) {
                    _nameError.value = application.getString(R.string.field_must_be_completed)
                    return
                } else if (!Patterns.EMAIL_ADDRESS.matcher(event.email).matches()) {
                    _emailError.value = application.getString(R.string.error_invalid_email)
                    return
                }

                useCases.register(email = event.email, password = event.password, name = event.name)
                    .onEach { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                Log.d("register", "success ${resource.data?.email}")
                                emitEvent(
                                    UiEvent.Navigate(
                                        route = Screen.TurnOnLocation.route,
                                        popUpTo = UiEvent.Navigate.PopUpTo(route = NavigationGraphs.Authentication, inclusive = true)
                                    )
                                )
                            }
                            is Resource.Loading -> {

                            }
                            is Resource.Error -> {
                                Log.d("register", "error")
                                when (resource.exception) {
                                    is FirebaseAuthWeakPasswordException -> {
                                        _passwordError.value =
                                            application.getString(R.string.error_weak_password)
                                    }
                                    is FirebaseAuthUserCollisionException -> {
                                        _emailError.value =
                                            application.getString(R.string.error_user_exists)
                                    }
                                }
                            }
                        }
                    }.launchIn(viewModelScope)
            }
            is RegisterEvents.OnNavigate -> {
                emitEvent(
                    UiEvent.Navigate(
                        route = event.route,
                        popUpTo = event.popUpTo
                    )
                )
            }
            is RegisterEvents.OnContinueWithFacebook -> {
                /* TODO */
            }
            is RegisterEvents.OnContinueWithGoogle -> {
                if (event.account != null) {
                    useCases.continueWithGoogle(event.account.idToken!!)
                        .onEach { resource ->
                            when (resource) {
                                is Resource.Success -> {
                                    emitEvent(
                                        UiEvent.Navigate(
                                            route = Screen.TurnOnLocation.route,
                                            popUpTo = UiEvent.Navigate.PopUpTo(route = NavigationGraphs.Authentication, inclusive = true)
                                        )
                                    )
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