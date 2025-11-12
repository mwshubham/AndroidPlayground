package com.example.android.systemdesign.di

import com.example.android.systemdesign.data.repository.SystemDesignRepositoryImpl
import com.example.android.systemdesign.domain.repository.SystemDesignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSystemDesignRepository(
        systemDesignRepositoryImpl: SystemDesignRepositoryImpl
    ): SystemDesignRepository
}
