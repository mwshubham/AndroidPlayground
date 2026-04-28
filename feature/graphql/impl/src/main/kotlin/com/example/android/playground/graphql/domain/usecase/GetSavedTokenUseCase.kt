package com.example.android.playground.graphql.domain.usecase

import com.example.android.playground.graphql.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedTokenUseCase @Inject constructor(
    private val repository: GitHubRepository,
) {
    operator fun invoke(): Flow<String> = repository.getToken()
}
