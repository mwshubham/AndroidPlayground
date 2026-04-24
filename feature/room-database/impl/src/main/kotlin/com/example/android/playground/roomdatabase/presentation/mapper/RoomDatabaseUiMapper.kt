package com.example.android.playground.roomdatabase.presentation.mapper

import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
import com.example.android.playground.roomdatabase.domain.model.BookWithTags
import com.example.android.playground.roomdatabase.presentation.model.AuthorWithBooksUiModel
import com.example.android.playground.roomdatabase.presentation.model.BookWithTagsUiModel

fun AuthorWithBooks.toUiModel(): AuthorWithBooksUiModel =
    AuthorWithBooksUiModel(
        authorName = author.name,
        email = author.email,
        website = author.website,
        books = books.map { it.title },
    )

fun BookWithTags.toUiModel(): BookWithTagsUiModel =
    BookWithTagsUiModel(
        title = book.title,
        publishYear = book.publishYear,
        genres = book.genres.joinToString(" · "),
        tags = tags.map { it.name },
    )
