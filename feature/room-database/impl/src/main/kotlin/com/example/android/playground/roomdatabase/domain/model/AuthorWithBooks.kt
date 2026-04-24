package com.example.android.playground.roomdatabase.domain.model

data class AuthorWithBooks(
    val author: Author,
    val books: List<Book>,
)
