package com.goto_delivery.pgoto.ui.screens.restaurant_menu

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.core.app.ApplicationProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.goto_delivery.pgoto.R
import com.goto_delivery.pgoto.data.core.test_tags.TestTags
import com.goto_delivery.pgoto.ui.MainActivity
import com.goto_delivery.pgoto.ui.screens.restaurants.RestaurantListScreen
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
class RestaurantMenuTest {

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
                    composable(
                        route = Screen.RestaurantList.route
                    ) {
                        RestaurantListScreen(onNavigate = { destination ->
                            navController.navigate(destination.route)
                        })
                    }

                    composable(
                        route = Screen.RestaurantMenu.route,
                    ) {
                        RestaurantMenu(onPopBackStack = {})
                    }
                }
            }
        }
    }

    @Test
    fun addFoodToCartWithoutSelectingASuggestion_isSuccess() {
        /* Click on restaurant */
        composeRule.onAllNodesWithTag(testTag = TestTags.RESTAURANT).onFirst().performClick()

        /* Click on food */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD)[1].performClick()

        /* Add the food to cart */
        composeRule.onNodeWithTag(testTag = TestTags.ADD_TO_CART).performClick()
    }

    @Test
    fun addFoodToCartAndSelectOneSuggestion_isSuccess() {
        /* Click on restaurant */
        composeRule.onAllNodesWithTag(testTag = TestTags.RESTAURANT).onFirst().performClick()

        /* Click on food */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD)[1].performClick()

        /* Select a suggestion */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD_SUGGESTION).onFirst()
            .assertIsOff().performClick()

        /* Add the food to cart */
        composeRule.onNodeWithTag(testTag = TestTags.ADD_TO_CART).performClick()
    }

    @Test
    fun addFoodToCartAndUnselectOneSuggestion_isSuccess() {
        /* Click on restaurant */
        composeRule.onAllNodesWithTag(testTag = TestTags.RESTAURANT).onFirst().performClick()

        /* Click on food */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD)[1].performClick()

        /* Select a suggestion */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD_SUGGESTION).onFirst()
            .assertIsOff().performClick()

        /* Unselect a suggestion */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD_SUGGESTION).onFirst()
            .assertIsOn().performClick()

        /* Add the food to cart */
        composeRule.onNodeWithTag(testTag = TestTags.ADD_TO_CART).performClick()
    }

    @Test
    fun increaseFoodQuantity_byOne() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        /* Click on restaurant */
        composeRule.onAllNodesWithTag(testTag = TestTags.RESTAURANT).onFirst().performClick()

        /* Click on food */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD)[1].performClick()

        /* Add the food to cart */
        composeRule.onNodeWithTag(testTag = TestTags.ADD_TO_CART).performClick()

        /* Increase quantity */
        composeRule.onNodeWithContentDescription(context.getString(R.string.increase_quantity))
            .assertExists().performClick()
    }

    @Test
    fun decreaseFoodQuantity_byOne() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        /* Click on restaurant */
        composeRule.onAllNodesWithTag(testTag = TestTags.RESTAURANT).onFirst().performClick()

        /* Click on food */
        composeRule.onAllNodesWithTag(testTag = TestTags.FOOD)[1].performClick()

        /* Add the food to cart */
        composeRule.onNodeWithTag(testTag = TestTags.ADD_TO_CART).performClick()

        /* Decrease quantity */
        composeRule.onNodeWithContentDescription(context.getString(R.string.decrease_quantity))
            .assertExists().performClick()
    }
}