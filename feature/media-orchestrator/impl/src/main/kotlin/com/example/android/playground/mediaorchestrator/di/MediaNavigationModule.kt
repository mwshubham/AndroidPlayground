package com.example.android.playground.mediaorchestrator.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.mediaorchestrator.api.MediaOrchestratorRoute
import com.example.android.playground.mediaorchestrator.presentation.screen.MediaOrchestratorScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object MediaNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<MediaOrchestratorRoute> {
                MediaOrchestratorScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
