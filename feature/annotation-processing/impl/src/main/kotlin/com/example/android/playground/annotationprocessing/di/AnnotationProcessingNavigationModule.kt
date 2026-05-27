package com.example.android.playground.annotationprocessing.di

import com.example.android.playground.annotationprocessing.api.AnnotationProcessingRoute
import com.example.android.playground.annotationprocessing.presentation.screen.AnnotationProcessingScreen
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object AnnotationProcessingNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<AnnotationProcessingRoute> {
                AnnotationProcessingScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}
