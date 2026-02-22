package com.example.android.playground.login.di

import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.login.api.LoginRoute
import com.example.android.playground.login.presentation.LoginScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object LoginNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<LoginRoute> {
                LoginScreen(
                    onNavigateBack = {
                        navigator.goBack()
                    }
                )
            }
        }
}
