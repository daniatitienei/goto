package com.goto_delivery.pgoto.ui.screens.restaurants

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.goto_delivery.pgoto.data.core.test_tags.TestTags
import com.goto_delivery.pgoto.ui.MainActivity
import com.goto_delivery.pgoto.ui.theme.GotoTheme
import com.goto_delivery.pgoto.ui.utils.Screen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@HiltAndroidTest
class RestaurantListScreenTest {

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
                NavHost(
                    navController = navController,
                    startDestination = Screen.RestaurantList.route
                ) {
                    composable(Screen.RestaurantList.route) {
                        RestaurantListScreen()
                    }
                }
            }
        }
    }

    @Test
    fun clickToggleFilterChip_isSelected() {
        composeRule.onAllNodesWithTag(testTag = TestTags.FILTER_CHIP).assertAll(isNotSelected())
        composeRule.onAllNodesWithTag(testTag = TestTags.FILTER_CHIP).onFirst().performClick()
        composeRule.onAllNodesWithTag(testTag = TestTags.FILTER_CHIP).onFirst().assertIsSelected()
    }
}