package com.example.android.playground.grpc.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.grpc.api.GrpcRoute
import com.example.android.playground.grpc.presentation.screen.GrpcScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object GrpcNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<GrpcRoute> {
                GrpcScreen(onNavigateBack = { navigator.goBack() })
            }
        }
}
