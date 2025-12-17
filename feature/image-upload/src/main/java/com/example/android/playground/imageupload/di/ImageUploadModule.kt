package com.example.android.playground.imageupload.di

import com.example.android.playground.imageupload.data.repository.ImageUploadRepositoryImpl
import com.example.android.playground.imageupload.domain.repository.ImageUploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ImageUploadModule {

    @Binds
    @Singleton
    fun bindImageUploadRepository(
        imageUploadRepositoryImpl: ImageUploadRepositoryImpl
    ): ImageUploadRepository
}
