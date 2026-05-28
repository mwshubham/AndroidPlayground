package com.example.android.playground.media3player.di

import com.example.android.playground.media3player.data.repository.Media3PlayerRepositoryImpl
import com.example.android.playground.media3player.domain.repository.Media3PlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface Media3PlayerModule {
    @Binds
    @Singleton
    fun bindMedia3PlayerRepository(impl: Media3PlayerRepositoryImpl): Media3PlayerRepository
}
