package com.example.android.playground.deviceclassifier.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.deviceclassifier.api.DeviceClassifierRoute
import com.example.android.playground.deviceclassifier.presentation.screen.DeviceClassifierScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object DeviceClassifierNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<DeviceClassifierRoute> {
                DeviceClassifierScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
