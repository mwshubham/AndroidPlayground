package com.example.android.playground.userinitiatedservice.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.userinitiatedservice.api.UserInitiatedServiceRoute
import com.example.android.playground.userinitiatedservice.presentation.screen.UserInitiatedServiceScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object UserInitiatedServiceNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<UserInitiatedServiceRoute> {
                UserInitiatedServiceScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
