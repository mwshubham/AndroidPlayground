package com.example.android.systemdesign.imageupload.di

import com.example.android.systemdesign.imageupload.data.repository.ImageUploadStateRepositoryImpl
import com.example.android.systemdesign.imageupload.domain.repository.ImageUploadStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageUploadRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindImageUploadStateRepository(
        imageUploadStateRepositoryImpl: ImageUploadStateRepositoryImpl
    ): ImageUploadStateRepository
}
