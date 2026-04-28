package com.example.android.playground.graphql.presentation.model

data class RepoUiModel(
    val name: String,
    val nameWithOwner: String,
    val description: String,
    val starsDisplay: String,
    val language: String?,
    val languageColor: String?,
    val url: String,
    val ownerLogin: String,
)
