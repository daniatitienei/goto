package com.goto_delivery.pgoto.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.ui.utils.Screen
import com.goto_delivery.pgoto.ui.utils.UiEvent
import com.goto_delivery.pgoto.ui.utils.authentication.google.googleSignInActivityResult
import com.goto_delivery.pgoto.ui.utils.components.GotoTextField
import com.goto_delivery.pgoto.ui.utils.components.ThirdPartyAuthenticationMethod
import com.goto_delivery.pgoto.ui.utils.enums.TextFieldType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isPasswordObscured by remember {
        mutableStateOf(true)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> Unit
            }
        }
    }

    val signInRequestCode = 1

    val googleAuthResultLauncher =
        googleSignInActivityResult { account ->
            viewModel.onEvent(LoginEvents.OnContinueWithGoogle(account))
        }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(2.5f),
                verticalArrangement = Arrangement.spacedBy(
                    10.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                /* Header */
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_back),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(text = stringResource(id = R.string.enter_account))
                }

                Spacer(modifier = Modifier.height(15.dp))

                GotoTextField(
                    value = email,
                    onValueChange = { email = it },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = stringResource(id = R.string.email)
                        )
                    },
                    onImeAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
                    placeholder = stringResource(id = R.string.email),
                    keyboardType = KeyboardType.Email,
                    textFieldType = TextFieldType.EMAIL,
                    errorMessage = viewModel.emailError.value
                )

                GotoTextField(
                    value = password,
                    onValueChange = { password = it },
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            keyboardController?.hide()
                        }
                    },
                    placeholder = stringResource(id = R.string.password),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = stringResource(id = R.string.password)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isPasswordObscured = !isPasswordObscured
                            }
                        ) {
                            Icon(
                                if (isPasswordObscured) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = stringResource(
                                    id = if (isPasswordObscured) R.string.password_obscured
                                    else R.string.password_is_not_obscured
                                )
                            )
                        }
                    },
                    isObscured = isPasswordObscured,
                    textFieldType = TextFieldType.PASSWORD,
                    keyboardType = KeyboardType.Password,
                    errorMessage = viewModel.passwordError.value
                )

                Spacer(modifier = Modifier.height(5.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(
                            LoginEvents.OnValidate(
                                email, password
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /* Third party authentication methods */
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.or_continue_with))

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            15.dp,
                            alignment = Alignment.CenterHorizontally
                        )
                    ) {
                        ThirdPartyAuthenticationMethod(
                            imagePainter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = stringResource(id = R.string.google_authentication),
                            onClick = {
                                googleAuthResultLauncher.launch(signInRequestCode)
                            }
                        )

                        ThirdPartyAuthenticationMethod(
                            imagePainter = painterResource(id = R.drawable.ic_facebook_logo),
                            contentDescription = stringResource(id = R.string.facebook_authentication),
                            onClick = {
                                viewModel.onEvent(LoginEvents.OnContinueWithFacebook)
                            }
                        )
                    }
                }

                /* Redirect to Login screen */
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(
                                LoginEvents.OnNavigate(
                                    route = Screen.Register.route,
                                    popUpTo = UiEvent.Navigate.PopUpTo(
                                        route = Screen.Login.route,
                                        inclusive = true
                                    )
                                )
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.do_not_have_an_account) + '\n'
                                    + stringResource(id = R.string.register),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
