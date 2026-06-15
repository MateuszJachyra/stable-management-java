package com.example.stable_management_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.stable_management_mobile.navigation.AppNavigation
import com.example.stable_management_mobile.ui.theme.StablemanagementmobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StablemanagementmobileTheme {
                AppNavigation()
            }
        }
    }
}