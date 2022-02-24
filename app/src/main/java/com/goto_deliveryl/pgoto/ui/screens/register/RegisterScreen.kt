package com.goto_deliveryl.pgoto.ui.screens.register

import android.app.Application
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goto_deliveryl.pgoto.R
import com.goto_deliveryl.pgoto.ui.theme.GotoTheme
import com.goto_deliveryl.pgoto.ui.theme.Lime900
import com.goto_deliveryl.pgoto.ui.utils.Screens
import com.goto_deliveryl.pgoto.ui.utils.UiEvent
import com.goto_deliveryl.pgoto.ui.utils.components.ThirdPartyAuthenticationMethod
import com.goto_deliveryl.pgoto.ui.utils.enum.TextFieldType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigate: (UiEvent.Navigate) -> Unit,
) {
    var email by remember {
        mutableStateOf("")
    }

    var name by remember {
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
                        text = stringResource(id = R.string.welcome),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(text = stringResource(id = R.string.create_an_account))
                }

                Spacer(modifier = Modifier.height(15.dp))

                RegistrationTextField(
                    value = email,
                    onValueChange = { email = it },
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, contentDescription = null)
                    },
                    onImeAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
                    placeholder = stringResource(id = R.string.email),
                    keyboardType = KeyboardType.Email,
                    textFieldType = TextFieldType.EMAIL,
                    errorMessage = viewModel.emailError.value
                )

                RegistrationTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(id = R.string.name),
                    onImeAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                    },
                    textFieldType = TextFieldType.NAME,
                    capitalization = KeyboardCapitalization.Words,
                    errorMessage = viewModel.nameError.value
                )

                RegistrationTextField(
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
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isPasswordObscured = !isPasswordObscured
                            }
                        ) {
                            Icon(
                                if (isPasswordObscured) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    isObscured = isPasswordObscured,
                    textFieldType = TextFieldType.PASSWORD,
                    keyboardType = KeyboardType.Password,
                    errorMessage = viewModel.passwordError.value
                )

                Spacer(modifier = Modifier.height(5.dp))

                ElevatedButton(
                    onClick = {
                        viewModel.onEvent(
                            RegisterViewModel.RegisterEvents.OnValidate(
                                email, name, password
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
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
                                viewModel.onEvent(RegisterViewModel.RegisterEvents.OnContinueWithGoogle)
                            }
                        )

                        ThirdPartyAuthenticationMethod(
                            imagePainter = painterResource(id = R.drawable.ic_facebook_logo),
                            contentDescription = stringResource(id = R.string.google_authentication),
                            onClick = {
                                viewModel.onEvent(RegisterViewModel.RegisterEvents.OnContinueWithFacebook)
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
                                RegisterViewModel.RegisterEvents.OnNavigate(route = Screens.Login.route)
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.already_have_an_account) + '\n'
                                    + stringResource(id = R.string.login),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistrationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (KeyboardActionScope) -> Unit,
    shape: Shape = RoundedCornerShape(10.dp),
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String? = null,
    placeholder: String?,
    textFieldType: TextFieldType = TextFieldType.TEXT,
    isObscured: Boolean? = null,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None
) {
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Lime900,
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            placeholder = {
                placeholder?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (textFieldType == TextFieldType.PASSWORD && isObscured == true) PasswordVisualTransformation() else VisualTransformation.None,
            shape = shape,
            leadingIcon = leadingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            trailingIcon = trailingIcon,
            keyboardActions = KeyboardActions(
                onDone = onImeAction,
                onNext = onImeAction,
                onSearch = onImeAction,
                onGo = onImeAction,
                onPrevious = onImeAction,
                onSend = onImeAction
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
                capitalization = capitalization
            ),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(5.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun LightRegisterPreview() {
    GotoTheme {
        RegisterScreen(
            onNavigate = {},
            viewModel = RegisterViewModel(application = Application())
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DarkRegisterPreview() {
    GotoTheme {
        RegisterScreen(
            onNavigate = {},
            viewModel = RegisterViewModel(application = Application())
        )
    }
}