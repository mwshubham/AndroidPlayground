package com.example.android.playground.roomdatabase.domain.usecase

import com.example.android.playground.roomdatabase.domain.model.BookWithTags
import com.example.android.playground.roomdatabase.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksWithTagsUseCase
    @Inject
    constructor(private val repository: LibraryRepository) {
        operator fun invoke(): Flow<List<BookWithTags>> = repository.getBooksWithTags()
    }
