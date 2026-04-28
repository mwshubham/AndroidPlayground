package com.example.android.playground.graphql.domain.usecase

import com.example.android.playground.graphql.domain.repository.GitHubRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val repository: GitHubRepository,
) {
    suspend operator fun invoke(token: String) = repository.saveToken(token)
}
