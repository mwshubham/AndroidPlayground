package com.example.android.systemdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.android.systemdesign.navigation.AppNavigation
import com.example.android.systemdesign.ui.theme.AndroidSystemDesignTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSystemDesignTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
