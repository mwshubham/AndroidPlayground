package com.example.android.playground.sse.domain.model

data class WikipediaChange(
    val type: String,
    val title: String,
    val user: String,
    val wiki: String,
    val comment: String,
    val timestampMs: Long,
)
