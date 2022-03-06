package com.goto_delivery.pgoto.ui.utils.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.ui.theme.GotoTheme

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onSearch: (KeyboardActionScope) -> Unit,
    onClear: () -> Unit,
) {
    Box {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = onSearch
            ),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer),
            cursorBrush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary
                )
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)

                        Spacer(modifier = Modifier.width(8.dp))

                        AnimatedVisibility(
                            visible = value.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            innerTextField()
                        }
                    }

                    AnimatedVisibility(
                        visible = value.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(
                                id = R.string.clear_text
                            ),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    onClear()
                                }
                        )
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = value.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarPreviewLight() {
    GotoTheme {
        CenterAlignedTopAppBar(
            title = {
                SearchBar(
                    value = "321321321",
                    onValueChange = {},
                    onSearch = {},
                    placeholder = "Cauta",
                    onClear = {}
                )
            }
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SearchBarPreviewDark() {
    GotoTheme {
        CenterAlignedTopAppBar(
            title = {
                SearchBar(
                    value = "",
                    onValueChange = {},
                    onSearch = {},
                    placeholder = "Cauta",
                    onClear = {}
                )
            }
        )
    }
}