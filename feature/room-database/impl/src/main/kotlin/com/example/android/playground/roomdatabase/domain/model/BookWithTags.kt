package com.example.android.playground.roomdatabase.domain.model

data class BookWithTags(
    val book: Book,
    val tags: List<Tag>,
)
