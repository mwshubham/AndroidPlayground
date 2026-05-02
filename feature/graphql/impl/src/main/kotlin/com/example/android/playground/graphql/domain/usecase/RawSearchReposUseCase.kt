package com.example.android.playground.graphql.domain.usecase

import com.example.android.playground.graphql.domain.model.RepoSearchResult
import com.example.android.playground.graphql.domain.repository.GitHubRepository
import javax.inject.Inject

class RawSearchReposUseCase
    @Inject
    constructor(
        private val repository: GitHubRepository,
    ) {
        suspend operator fun invoke(
            query: String,
            token: String,
            after: String? = null,
        ): RepoSearchResult = repository.searchRepos(query = query, token = token, after = after)
    }
