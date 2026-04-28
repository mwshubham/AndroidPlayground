package com.example.android.playground.graphql.domain.model

data class RepoSearchResult(
    val repos: List<Repo>,
    val totalCount: Int,
    val hasNextPage: Boolean,
    val endCursor: String?,
)
