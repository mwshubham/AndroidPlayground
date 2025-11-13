package com.example.android.systemdesign.imageupload.di

import com.example.android.systemdesign.imageupload.data.repository.ImageUploadRepositoryImpl
import com.example.android.systemdesign.imageupload.domain.repository.ImageUploadRepository
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
