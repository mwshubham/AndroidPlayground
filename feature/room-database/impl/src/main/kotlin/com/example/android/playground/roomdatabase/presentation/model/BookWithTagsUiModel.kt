package com.example.android.playground.roomdatabase.presentation.model

data class BookWithTagsUiModel(
    val title: String,
    val publishYear: Int,
    val genres: String,
    val tags: List<String>,
)
