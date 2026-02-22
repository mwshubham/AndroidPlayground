package com.example.android.playground.imageupload.di

import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.imageupload.api.ImageUploadRoute
import com.example.android.playground.imageupload.presentation.ImageUploadScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object ImageUploadNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<ImageUploadRoute> {
                ImageUploadScreen(
                    onNavigateBack = {
                        navigator.goBack()
                    }
                )
            }
        }
}
