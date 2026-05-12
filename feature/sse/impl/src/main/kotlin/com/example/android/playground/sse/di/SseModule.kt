package com.example.android.playground.sse.di

import com.example.android.playground.sse.data.repository.SseRepositoryImpl
import com.example.android.playground.sse.domain.repository.SseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SseModule {
    @Binds
    @Singleton
    abstract fun bindSseRepository(impl: SseRepositoryImpl): SseRepository

    companion object {
        @Named("sse")
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

        @Named("sse")
        @Provides
        @Singleton
        fun provideJson(): Json =
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
    }
}
