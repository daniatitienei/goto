package com.goto_delivery.pgoto.ui.screens.login

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.ui.MainActivity
import com.goto_delivery.pgoto.ui.screens.register.RegisterScreen
import com.goto_delivery.pgoto.ui.theme.GotoTheme
import com.goto_delivery.pgoto.ui.utils.Screen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@HiltAndroidTest
class LoginScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.setContent {
            val navController = rememberNavController()

            GotoTheme {
                NavHost(navController = navController, startDestination = Screen.Login.route) {
                    composable(route = Screen.Login.route) {
                        LoginScreen(
                            onNavigate = {}
                        )
                    }
                }
            }

        }
    }

    @Test
    fun loginInWithAccountWithEmailAndPassword_isSuccess() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        /* Fills all text fields */
        composeRule.onNodeWithText(context.getString(R.string.email))
            .performClick()
            .performTextInput("marius@gmail.com")

        composeRule.onNodeWithText(context.getString(R.string.password))
            .performClick()
            .performTextInput("dani94552")

        /* Creates the account */
        composeRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
    }

    @Test
    fun continueWithGoogle_isSuccess() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.onNodeWithContentDescription(context.getString(R.string.google_authentication))
            .performClick()
    }
}