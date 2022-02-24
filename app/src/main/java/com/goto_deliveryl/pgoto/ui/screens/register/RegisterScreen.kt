package com.goto_deliveryl.pgoto.ui.screens.register

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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goto_deliveryl.pgoto.R
import com.goto_deliveryl.pgoto.ui.theme.GotoTheme
import com.goto_deliveryl.pgoto.ui.theme.Lime900
import com.goto_deliveryl.pgoto.ui.utils.components.ThirdPartyAuthenticationMethod
import com.goto_deliveryl.pgoto.ui.utils.enum.TextFieldType
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun RegisterScreen() {

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

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var isConfirmPasswordObscured by remember {
        mutableStateOf(true)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
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
                    textFieldType = TextFieldType.EMAIL
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
                    capitalization = KeyboardCapitalization.Words
                )

                RegistrationTextField(
                    value = password,
                    onValueChange = { password = it },
                    onImeAction = { focusManager.moveFocus(focusDirection = FocusDirection.Down) },
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
                                painter = painterResource(id = if (isPasswordObscured) R.drawable.ic_cutted_eye else R.drawable.ic_eye),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    isObscured = isPasswordObscured,
                    textFieldType = TextFieldType.PASSWORD,
                    keyboardType = KeyboardType.Password
                )

                RegistrationTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            keyboardController?.hide()
                        }
                    },
                    placeholder = stringResource(id = R.string.confirm_password),
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isConfirmPasswordObscured = !isConfirmPasswordObscured
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = if (isConfirmPasswordObscured) R.drawable.ic_cutted_eye else R.drawable.ic_eye),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    isObscured = isConfirmPasswordObscured,
                    textFieldType = TextFieldType.PASSWORD,
                    keyboardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(5.dp))

                ElevatedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(0.6f)) {
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
                            onClick = { /*TODO*/ }
                        )

                        ThirdPartyAuthenticationMethod(
                            imagePainter = painterResource(id = R.drawable.ic_facebook_logo),
                            contentDescription = stringResource(id = R.string.google_authentication),
                            onClick = { /*TODO*/ }
                        )
                    }
                }

                /* Redirect to Login screen */
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    TextButton(onClick = { /*TODO*/ }) {
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
        RegisterScreen()
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DarkRegisterPreview() {
    GotoTheme {
        RegisterScreen()
    }
}