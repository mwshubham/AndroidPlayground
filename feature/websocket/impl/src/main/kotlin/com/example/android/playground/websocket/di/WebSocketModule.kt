package com.example.android.playground.websocket.di

import com.example.android.playground.websocket.data.repository.WebSocketRepositoryImpl
import com.example.android.playground.websocket.domain.repository.WebSocketRepository
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
abstract class WebSocketModule {
    @Binds
    @Singleton
    abstract fun bindWebSocketRepository(impl: WebSocketRepositoryImpl): WebSocketRepository

    companion object {
        @Named("websocket")
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

        @Named("websocket")
        @Provides
        @Singleton
        fun provideJson(): Json =
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
    }
}
