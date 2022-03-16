package com.goto_delivery.pgoto.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.goto_delivery.pgoto.ui.screens.cart.CartScreen
import com.goto_delivery.pgoto.ui.screens.location.TurnOnLocationScreen
import com.goto_delivery.pgoto.ui.screens.login.LoginScreen
import com.goto_delivery.pgoto.ui.screens.register.RegisterScreen
import com.goto_delivery.pgoto.ui.screens.restaurant_menu.RestaurantMenu
import com.goto_delivery.pgoto.ui.screens.restaurants.RestaurantListScreen
import com.goto_delivery.pgoto.ui.utils.NavigationGraphs
import com.goto_delivery.pgoto.ui.utils.Screen

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationGraphs.Restaurant) {
        authenticationGraph(navController = navController)

        composable(route = Screen.TurnOnLocation.route) {
            TurnOnLocationScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true

                        destination.popUpTo?.let { popUpTo ->
                            popUpTo(popUpTo.route) {
                                inclusive = popUpTo.inclusive
                            }
                        }
                    }
                }
            )
        }

        restaurantGraph(navController = navController)
    }
}

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    navigation(startDestination = Screen.Register.route, route = NavigationGraphs.Authentication) {
        composable(Screen.Register.route) {
            RegisterScreen { destination ->
                navController.navigate(destination.route) {
                    launchSingleTop = true

                    destination.popUpTo?.let { popUpTo ->
                        popUpTo(popUpTo.route) {
                            inclusive = popUpTo.inclusive
                        }
                    }
                }
            }
        }
        composable(Screen.Login.route) {
            LoginScreen { destination ->
                navController.navigate(destination.route) {
                    launchSingleTop = true

                    destination.popUpTo?.let { popUpTo ->
                        popUpTo(popUpTo.route) {
                            inclusive = popUpTo.inclusive
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.restaurantGraph(navController: NavController) {
    navigation(startDestination = Screen.RestaurantList.route, route =  NavigationGraphs.Restaurant) {
        composable(route = Screen.RestaurantList.route) {
            RestaurantListScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screen.RestaurantMenu.route) {
            RestaurantMenu(
                onPopBackStack = {
                    navController.popBackStack()
                },
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screen.Cart.route) {
            CartScreen()
        }
    }
}