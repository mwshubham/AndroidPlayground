package com.example.android.playground.tictactoe.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.tictactoe.api.TicTacToeGameRoute
import com.example.android.playground.tictactoe.api.TicTacToeSetupRoute
import com.example.android.playground.tictactoe.presentation.screen.TicTacToeGameScreen
import com.example.android.playground.tictactoe.presentation.screen.TicTacToeSetupScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object TicTacToeNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<TicTacToeSetupRoute> {
                TicTacToeSetupScreen(
                    onNavigateBack = {
                        navigator.goBack()
                    },
                    onNavigateToGame = { route ->
                        navigator.goTo(route)
                    },
                )
            }

            entry<TicTacToeGameRoute> { route ->
                TicTacToeGameScreen(
                    route = route,
                    onNavigateBack = {
                        navigator.goBack()
                    },
                )
            }
        }
}
