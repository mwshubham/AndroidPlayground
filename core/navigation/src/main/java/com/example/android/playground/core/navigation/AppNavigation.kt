package com.example.android.playground.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppNavigation(
    navigator: AppNavigator,
    backStack: List<NavKey>,
    entryProviderScopes: Set<EntryProviderInstaller>,
) {
    NavDisplay(
        backStack = backStack,
        onBack = { navigator.goBack() },
        transitionSpec = Navigation3Transitions.horizontalSlideTransition(),
        popTransitionSpec = Navigation3Transitions.horizontalSlidePopTransition(),
        predictivePopTransitionSpec = Navigation3Transitions.predictivePopTransition(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entryProviderScopes.forEach { builder -> this.builder() }
        }
    )
}
