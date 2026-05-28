package com.example.android.playground.websocket.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.websocket.api.WebSocketRoute
import com.example.android.playground.websocket.presentation.screen.WebSocketScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object WebSocketNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<WebSocketRoute> {
                WebSocketScreen(onNavigateBack = { navigator.goBack() })
            }
        }
}
