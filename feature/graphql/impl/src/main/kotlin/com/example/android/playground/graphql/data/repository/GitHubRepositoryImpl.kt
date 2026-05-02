package com.example.android.playground.graphql.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.android.playground.graphql.data.network.GitHubGraphQLService
import com.example.android.playground.graphql.domain.model.Repo
import com.example.android.playground.graphql.domain.model.RepoSearchResult
import com.example.android.playground.graphql.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val GITHUB_TOKEN_KEY = stringPreferencesKey("github_pat")

internal class GitHubRepositoryImpl
    @Inject
    constructor(
        private val service: GitHubGraphQLService,
        private val dataStore: DataStore<Preferences>,
    ) : GitHubRepository {
        override suspend fun searchRepos(
            query: String,
            token: String,
            after: String?,
        ): RepoSearchResult {
            val response = service.searchRepos(query = query, token = token, after = after)

            val errors = response.errors
            if (!errors.isNullOrEmpty()) {
                error(errors.joinToString { it.message })
            }

            val searchData = checkNotNull(response.data) { "GraphQL response has no data" }
            val search = searchData.search

            val repos =
                search.edges.mapNotNull { edge ->
                    val node = edge.node
                    if (node.nameWithOwner.isBlank()) return@mapNotNull null
                    Repo(
                        name = node.name,
                        nameWithOwner = node.nameWithOwner,
                        description = node.description,
                        stars = node.stargazerCount,
                        language = node.primaryLanguage?.name,
                        languageColor = node.primaryLanguage?.color,
                        url = node.url,
                        ownerLogin = node.owner?.login.orEmpty(),
                        updatedAt = node.updatedAt,
                    )
                }

            return RepoSearchResult(
                repos = repos,
                totalCount = search.repositoryCount,
                hasNextPage = search.pageInfo.hasNextPage,
                endCursor = search.pageInfo.endCursor,
            )
        }

        override suspend fun saveToken(token: String) {
            dataStore.edit { it[GITHUB_TOKEN_KEY] = token }
        }

        override fun getToken(): Flow<String> = dataStore.data.map { it[GITHUB_TOKEN_KEY].orEmpty() }
    }
