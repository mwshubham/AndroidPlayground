package com.example.android.systemdesign.note.di

import android.content.Context
import androidx.room.Room
import com.example.android.systemdesign.note.data.local.NoteDao
import com.example.android.systemdesign.note.data.local.NoteDatabase
import com.example.android.systemdesign.note.data.repository.NoteRepositoryImpl
import com.example.android.systemdesign.note.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NoteModule {

    @Binds
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository

    companion object {

        @Provides
        @Singleton
        fun provideNoteDatabase(
            @ApplicationContext context: Context
        ): NoteDatabase {
            return Room.databaseBuilder(
                context = context.applicationContext,
                klass = NoteDatabase::class.java,
                name = NoteDatabase.DATABASE_NAME
            )
            .build()
        }

        @Provides
        fun provideNoteDao(database: NoteDatabase): NoteDao {
            return database.noteDao()
        }
    }
}
