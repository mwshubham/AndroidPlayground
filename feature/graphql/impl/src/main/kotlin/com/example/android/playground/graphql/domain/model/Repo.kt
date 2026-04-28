package com.example.android.playground.graphql.domain.model

data class Repo(
    val name: String,
    val nameWithOwner: String,
    val description: String?,
    val stars: Int,
    val language: String?,
    val languageColor: String?,
    val url: String,
    val ownerLogin: String,
    val updatedAt: String,
)
