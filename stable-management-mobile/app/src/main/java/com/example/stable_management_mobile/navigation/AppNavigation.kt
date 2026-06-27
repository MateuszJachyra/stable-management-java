package com.example.stable_management_mobile.navigation

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stable_management_mobile.ui.screens.login.LoginScreen
import com.example.stable_management_mobile.ui.screens.stables.StableDetailsScreen
import com.example.stable_management_mobile.ui.screens.stables.StablesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("stables") {
                    popUpTo("login") {inclusive = true}
                }
            })
        }

        composable("stables") {
            StablesScreen(onStableClicked = { stableName ->
                navController.navigate("stables/$stableName")
            })
        }

        composable(
            "stables/{stableName}",
            listOf(navArgument("stableName") {})
        ) {
            StableDetailsScreen()
        }
    }

}