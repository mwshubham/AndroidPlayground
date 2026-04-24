package com.example.android.playground.roomdatabase.presentation.model

data class AuthorWithBooksUiModel(
    val authorName: String,
    val email: String,
    val website: String,
    val books: List<String>,
)
