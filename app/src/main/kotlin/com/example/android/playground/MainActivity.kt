package com.example.android.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        val incomingMessage = intent.getStringExtra("message")
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(incomingMessage) {
                incomingMessage?.let { snackbarHostState.showSnackbar(it) }
            }
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
            ) {
                AppTheme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AppNavigation(
                            navigator = navigator,
                            backStack = navigator.backStack,
                            entryProviderScopes = entryProviderScopes
                        )
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding(),
                        )
                    }
                }
            }
        }
    }
}
