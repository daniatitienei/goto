package com.goto_delivery.pgoto.ui.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.goto_delivery.pgoto.ui.theme.Lime900
import com.goto_delivery.pgoto.ui.utils.enum.TextFieldType

@Composable
fun GotoTextField(
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