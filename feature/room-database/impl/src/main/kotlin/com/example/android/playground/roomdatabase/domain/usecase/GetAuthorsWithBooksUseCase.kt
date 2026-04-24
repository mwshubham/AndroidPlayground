package com.example.android.playground.roomdatabase.domain.usecase

import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthorsWithBooksUseCase
    @Inject
    constructor(private val repository: LibraryRepository) {
        operator fun invoke(): Flow<List<AuthorWithBooks>> = repository.getAuthorsWithBooks()
    }
