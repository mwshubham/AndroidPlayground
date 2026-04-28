package com.example.android.playground.graphql.domain.repository

import com.example.android.playground.graphql.domain.model.RepoSearchResult
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    suspend fun searchRepos(query: String, token: String, after: String? = null): RepoSearchResult
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String>
}
