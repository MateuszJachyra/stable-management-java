package com.example.stable_management_mobile.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Stables: Screen("stables")

    object StableDetails: Screen("stables/{stableName}") {
        fun createRoute(stableName: String) = "stables/$stableName"
    }

    object HorseDetails: Screen("horses/{horseId}") {
        fun createRoute(horseId: Int) = "horses/$horseId"
    }
}