package com.example.android.playground.roomdatabase.di

import android.content.Context
import androidx.room.Room
import com.example.android.playground.roomdatabase.data.local.LibraryDao
import com.example.android.playground.roomdatabase.data.local.LibraryDatabase
import com.example.android.playground.roomdatabase.data.repository.LibraryRepositoryImpl
import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RoomDatabaseModule {
    @Binds
    fun bindLibraryRepository(impl: LibraryRepositoryImpl): LibraryRepository

    companion object {
        @Provides
        @Singleton
        fun provideLibraryDatabase(
            @ApplicationContext context: Context,
        ): LibraryDatabase =
            Room
                .databaseBuilder(
                    context = context.applicationContext,
                    klass = LibraryDatabase::class.java,
                    name = LibraryDatabase.DATABASE_NAME,
                ).build()

        @Provides
        fun provideLibraryDao(database: LibraryDatabase): LibraryDao = database.libraryDao()
    }
}
