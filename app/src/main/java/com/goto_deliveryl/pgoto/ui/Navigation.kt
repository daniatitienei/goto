package com.goto_deliveryl.pgoto.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.goto_deliveryl.pgoto.ui.screens.register.RegisterScreen
import com.goto_deliveryl.pgoto.ui.utils.Routes
import com.goto_deliveryl.pgoto.ui.utils.Screens

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.AuthenticationGraph) {
        authenticationGraph(navController = navController)
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
                }
            }
        }
        composable(Screens.Login.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                Text(text = "login")
            }
        }
    }
}