package com.example.android.playground.mediaorchestrator.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.android.playground.mediaorchestrator.data.local.dao.MediaItemDao
import com.example.android.playground.mediaorchestrator.data.local.database.MediaDatabase
import com.example.android.playground.mediaorchestrator.data.repository.MediaRepositoryImpl
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MediaModule {
    @Binds
    @Singleton
    fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    companion object {
        @Provides
        @Singleton
        fun provideMediaDatabase(
            @ApplicationContext context: Context,
        ): MediaDatabase =
            Room
                .databaseBuilder(
                    context = context.applicationContext,
                    klass = MediaDatabase::class.java,
                    name = MediaDatabase.DATABASE_NAME,
                ).build()

        @Provides
        fun provideMediaItemDao(database: MediaDatabase): MediaItemDao = database.mediaItemDao()

        @Provides
        fun provideWorkManager(
            @ApplicationContext context: Context,
        ): WorkManager = WorkManager.getInstance(context)
    }
}
