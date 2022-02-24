package com.goto_deliveryl.pgoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.goto_deliveryl.pgoto.ui.screens.register.RegisterScreen
import com.goto_deliveryl.pgoto.ui.theme.GotoTheme

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val systemUiController = rememberSystemUiController()

            GotoTheme {
                val backgroundColor = MaterialTheme.colorScheme.background

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = backgroundColor
                    )
                }

                RegisterScreen()
            }
        }
    }
}
