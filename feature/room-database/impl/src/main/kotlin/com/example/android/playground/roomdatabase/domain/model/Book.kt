package com.example.android.playground.roomdatabase.domain.model

data class Book(
    val id: Long,
    val title: String,
    val authorId: Long,
    val publishYear: Int,
    val genres: List<String>,
)
