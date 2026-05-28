package com.example.android.playground.deviceclassifier.di

import com.example.android.playground.deviceclassifier.data.repository.DeviceClassifierRepositoryImpl
import com.example.android.playground.deviceclassifier.domain.repository.DeviceClassifierRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceClassifierModule {
    @Binds
    abstract fun bindDeviceClassifierRepository(impl: DeviceClassifierRepositoryImpl): DeviceClassifierRepository
}
