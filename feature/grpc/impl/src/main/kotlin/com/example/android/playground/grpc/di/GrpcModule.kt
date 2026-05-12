package com.example.android.playground.grpc.di

import com.example.android.playground.grpc.data.repository.GrpcRepositoryImpl
import com.example.android.playground.grpc.domain.repository.GrpcRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import io.grpc.okhttp.OkHttpChannelBuilder
import javax.inject.Singleton

private const val ELIZA_HOST = "demo.connectrpc.com"
private const val ELIZA_PORT = 443

@Module
@InstallIn(SingletonComponent::class)
abstract class GrpcModule {
    @Binds
    @Singleton
    abstract fun bindGrpcRepository(impl: GrpcRepositoryImpl): GrpcRepository

    companion object {
        @Provides
        @Singleton
        fun provideManagedChannel(): ManagedChannel =
            OkHttpChannelBuilder
                .forAddress(ELIZA_HOST, ELIZA_PORT)
                .useTransportSecurity()
                .build()
    }
}
