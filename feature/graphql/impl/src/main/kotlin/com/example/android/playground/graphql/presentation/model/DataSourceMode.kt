package com.example.android.playground.graphql.presentation.model

/**
 * Selects which HTTP transport executes the GraphQL query.
 *
 * Both modes send identical requests to https://api.github.com/graphql and
 * produce the same [com.example.android.playground.graphql.domain.model.RepoSearchResult].
 * The toggle lets you observe the difference in approach, not in result.
 *
 * | Aspect            | RAW_OKHTTP                          | APOLLO                              |
 * |-------------------|-------------------------------------|-------------------------------------|
 * | Query document    | String literal in GitHubGraphQLService | .graphql file, compiled at build time |
 * | Type safety       | Manual DTO + kotlinx.serialization  | Generated Kotlin classes            |
 * | JSON parsing      | You write the DTOs                  | Apollo generates them               |
 * | Variable handling | Build Map<String, String?> manually | Constructor parameters              |
 * | Union types       | Check nameWithOwner.isBlank()       | `onRepository` accessor             |
 * | Error handling    | response.errors list (manual check) | response.errors list (same)         |
 */
enum class DataSourceMode(val label: String) {
    RAW_OKHTTP("Raw OkHttp"),
    APOLLO("Apollo Kotlin"),
}
