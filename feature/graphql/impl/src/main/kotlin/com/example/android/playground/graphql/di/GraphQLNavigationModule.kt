package com.example.android.playground.graphql.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.graphql.api.GitHubExplorerRoute
import com.example.android.playground.graphql.presentation.screen.GitHubExplorerScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object GraphQLNavigationModule {

    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<GitHubExplorerRoute> {
                GitHubExplorerScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
