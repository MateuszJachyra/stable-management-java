package com.example.stable_management_mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stable_management_mobile.ui.screens.horse.HorseDetailsScreen
import com.example.stable_management_mobile.ui.screens.login.LoginScreen
import com.example.stable_management_mobile.ui.screens.stables.StableDetailsScreen
import com.example.stable_management_mobile.ui.screens.stables.StablesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.Stables.route) {
                    popUpTo(Screen.Login.route) {inclusive = true}
                }
            })
        }

        composable(Screen.Stables.route) {
            StablesScreen(onStableClicked = { stableName ->
                navController.navigate(Screen.StableDetails.createRoute(stableName))
            })
        }

        composable(
            route = Screen.StableDetails.route,
            arguments = listOf(navArgument("stableName") { type = NavType.StringType })
        ) {
            StableDetailsScreen(onHorseClicked = { horseId ->
                navController.navigate(Screen.HorseDetails.createRoute(horseId))
            })
        }

        composable(
            route = Screen.HorseDetails.route,
            arguments = listOf(navArgument("horseId") { type = NavType.IntType })
        ) {
            HorseDetailsScreen()
        }
    }

}