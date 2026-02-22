package com.example.android.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.android.playground.analytics.AnalyticsHelper
import com.example.android.playground.analytics.LocalAnalyticsHelper
import com.example.android.playground.core.navigation.AppNavigation
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var navigator: AppNavigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
            ) {
                AppTheme {
                    AppNavigation(
                        navigator = navigator,
                        backStack = navigator.backStack,
                        entryProviderScopes = entryProviderScopes
                    )
                }
            }
        }
    }
}
