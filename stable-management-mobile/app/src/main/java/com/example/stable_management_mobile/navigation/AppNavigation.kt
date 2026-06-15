package com.example.stable_management_mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stable_management_mobile.ui.screens.login.LoginScreen
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
            StablesScreen()
        }
    }

}