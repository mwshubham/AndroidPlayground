package com.example.android.playground.media3player.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.media3player.api.Media3PlayerRoute
import com.example.android.playground.media3player.presentation.screen.Media3PlayerScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object Media3PlayerNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<Media3PlayerRoute> {
                Media3PlayerScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
