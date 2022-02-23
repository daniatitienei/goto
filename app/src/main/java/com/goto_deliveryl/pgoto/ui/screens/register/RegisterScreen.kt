package com.goto_deliveryl.pgoto.ui.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.goto_deliveryl.pgoto.R
import com.goto_deliveryl.pgoto.ui.theme.GotoTheme

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen() {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.welcome),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    GotoTheme {
        RegisterScreen()
    }
}