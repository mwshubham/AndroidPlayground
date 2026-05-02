package com.example.android.playground.roomdatabase.domain.repository

import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
import com.example.android.playground.roomdatabase.domain.model.BookWithTags
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getAuthorsWithBooks(): Flow<List<AuthorWithBooks>>

    fun getBooksWithTags(): Flow<List<BookWithTags>>

    suspend fun seedInitialData()
}
