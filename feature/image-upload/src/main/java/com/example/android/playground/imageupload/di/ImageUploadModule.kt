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
abstract class ImageUploadModule {

    @Binds
    @Singleton
    abstract fun bindImageUploadRepository(
        imageUploadRepositoryImpl: ImageUploadRepositoryImpl
    ): ImageUploadRepository
}
