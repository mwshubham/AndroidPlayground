package com.example.android.playground

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.feed.api.FeedRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator() : AppNavigator = AppNavigator(startDestination = FeedRoute)
}
