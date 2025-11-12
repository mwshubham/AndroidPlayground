package com.example.android.systemdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android.systemdesign.presentation.main.MainScreen
import com.example.android.systemdesign.ui.theme.AndroidSystemDesignTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSystemDesignTheme {
                MainScreen()
            }
        }
    }
}
