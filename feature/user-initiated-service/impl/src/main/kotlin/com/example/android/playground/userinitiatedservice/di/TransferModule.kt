package com.example.android.playground.userinitiatedservice.di

import android.app.job.JobScheduler
import android.content.Context
import androidx.room.Room
import com.example.android.playground.userinitiatedservice.data.local.dao.TransferItemDao
import com.example.android.playground.userinitiatedservice.data.local.database.TransferDatabase
import com.example.android.playground.userinitiatedservice.data.repository.TransferRepositoryImpl
import com.example.android.playground.userinitiatedservice.domain.repository.TransferRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TransferModule {

    @Binds
    @Singleton
    fun bindTransferRepository(impl: TransferRepositoryImpl): TransferRepository

    companion object {

        @Provides
        @Singleton
        fun provideTransferDatabase(
            @ApplicationContext context: Context,
        ): TransferDatabase =
            Room
                .databaseBuilder(
                    context = context.applicationContext,
                    klass = TransferDatabase::class.java,
                    name = TransferDatabase.DATABASE_NAME,
                ).build()

        @Provides
        fun provideTransferItemDao(database: TransferDatabase): TransferItemDao =
            database.transferItemDao()

        /**
         * JobScheduler is a system service; it is provided here so use-cases can inject it
         * without knowing about Android framework directly.
         *
         * WorkManager is intentionally NOT re-provided here — it is already bound in
         * SingletonComponent by the media-orchestrator module's MediaModule. Hilt resolves
         * the WorkManager binding from there for any injector in the app.
         */
        @Provides
        @Singleton
        fun provideJobScheduler(
            @ApplicationContext context: Context,
        ): JobScheduler = context.getSystemService(JobScheduler::class.java)
    }
}
