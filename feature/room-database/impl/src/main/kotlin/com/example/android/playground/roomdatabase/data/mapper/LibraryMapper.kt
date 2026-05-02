package com.example.android.playground.roomdatabase.data.mapper

import com.example.android.playground.roomdatabase.data.local.AuthorWithBooksRelation
import com.example.android.playground.roomdatabase.data.local.BookWithTagsRelation
import com.example.android.playground.roomdatabase.domain.model.Author
import com.example.android.playground.roomdatabase.domain.model.AuthorWithBooks
import com.example.android.playground.roomdatabase.domain.model.Book
import com.example.android.playground.roomdatabase.domain.model.BookWithTags
import com.example.android.playground.roomdatabase.domain.model.Tag

fun AuthorWithBooksRelation.toDomain(): AuthorWithBooks =
    AuthorWithBooks(
        author =
            Author(
                id = author.id,
                name = author.name,
                email = author.contact.email,
                website = author.contact.website,
            ),
        books =
            books.map { entity ->
                Book(
                    id = entity.id,
                    title = entity.title,
                    authorId = entity.authorId,
                    publishYear = entity.publishYear,
                    genres = entity.genres,
                )
            },
    )

fun BookWithTagsRelation.toDomain(): BookWithTags =
    BookWithTags(
        book =
            Book(
                id = book.id,
                title = book.title,
                authorId = book.authorId,
                publishYear = book.publishYear,
                genres = book.genres,
            ),
        tags = tags.map { entity -> Tag(id = entity.id, name = entity.name) },
    )
