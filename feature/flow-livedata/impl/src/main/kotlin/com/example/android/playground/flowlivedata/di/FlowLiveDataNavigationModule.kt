package com.example.android.playground.flowlivedata.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.flowlivedata.api.FlowLiveDataRoute
import com.example.android.playground.flowlivedata.presentation.screen.FlowLiveDataScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object FlowLiveDataNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<FlowLiveDataRoute> {
                FlowLiveDataScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
