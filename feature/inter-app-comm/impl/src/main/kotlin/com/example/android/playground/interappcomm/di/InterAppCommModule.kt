package com.example.android.playground.interappcomm.di

import com.example.android.playground.interappcomm.data.repository.InterAppCommRepositoryImpl
import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterAppCommModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: InterAppCommRepositoryImpl): InterAppCommRepository
}
