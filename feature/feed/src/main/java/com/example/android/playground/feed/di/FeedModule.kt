package com.example.android.playground.feed.di

import com.example.android.playground.feed.data.repository.FeedRepositoryImpl
import com.example.android.playground.feed.domain.repository.FeedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FeedModule {

    @Binds
    @Singleton
    fun bindFeedRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository
}
