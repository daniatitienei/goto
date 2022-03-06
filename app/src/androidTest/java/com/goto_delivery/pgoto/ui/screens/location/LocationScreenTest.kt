package com.goto_delivery.pgoto.ui.screens.location

import android.Manifest
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.ui.MainActivity
import com.goto_delivery.pgoto.ui.theme.GotoTheme
import com.goto_delivery.pgoto.ui.utils.Screen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@HiltAndroidTest
class LocationScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Before
    fun setUp() {
        composeRule.setContent {
            val navController = rememberNavController()

            GotoTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.TurnOnLocation.route
                ) {
                    composable(Screen.TurnOnLocation.route) {
                        TurnOnLocationScreen(onNavigate = {})
                    }
                }
            }
        }
    }

    @Test
    fun enablingLocation_isSuccess() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.onNodeWithText(context.getString(R.string.use_location)).performClick()
    }

}