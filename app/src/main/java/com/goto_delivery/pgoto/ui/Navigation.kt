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
import com.goto_delivery.pgoto.ui.screens.location.TurnOnLocationScreen
import com.goto_delivery.pgoto.ui.screens.login.LoginScreen
import com.goto_delivery.pgoto.ui.screens.register.RegisterScreen
import com.goto_delivery.pgoto.ui.screens.restaurants.RestaurantListScreen
import com.goto_delivery.pgoto.ui.utils.Routes
import com.goto_delivery.pgoto.ui.utils.Screens

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.TurnOnLocation.route) {
        authenticationGraph(navController = navController)

        composable(route = Screens.TurnOnLocation.route) {
            TurnOnLocationScreen(
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screens.RestaurantList.route) {
            RestaurantListScreen()
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    navigation(startDestination = Screens.Register.route, route = Routes.AuthenticationGraph) {
        composable(Screens.Register.route) {
            RegisterScreen { destination ->
                navController.navigate(destination.route) {
                    launchSingleTop = true

                    destination.popUpTo?.let { popUpTo ->
                        popUpTo(route = popUpTo.route) {
                            inclusive = popUpTo.inclusive
                        }
                    }
                }
            }
        }
        composable(Screens.Login.route) {
            LoginScreen { destination ->
                navController.navigate(destination.route) {
                    launchSingleTop = true
                }
            }
        }
    }
}