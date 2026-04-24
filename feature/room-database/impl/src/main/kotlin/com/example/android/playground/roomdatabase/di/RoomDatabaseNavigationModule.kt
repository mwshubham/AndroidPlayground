package com.example.android.playground.roomdatabase.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.roomdatabase.api.RoomDatabaseRoute
import com.example.android.playground.roomdatabase.presentation.screen.RoomDatabaseScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object RoomDatabaseNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<RoomDatabaseRoute> {
                RoomDatabaseScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
