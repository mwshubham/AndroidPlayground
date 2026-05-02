package com.example.android.playground.graphql.domain.usecase

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.example.android.playground.graphql.apollo.SearchReposQuery
import com.example.android.playground.graphql.domain.model.Repo
import com.example.android.playground.graphql.domain.model.RepoSearchResult
import javax.inject.Inject

/**
 * Executes the GitHub repository search using **Apollo Kotlin**.
 *
 * Compare this with [RawSearchReposUseCase] which uses raw OkHttp + manual JSON parsing.
 *
 * ## How Apollo works (step by step)
 *
 * 1. **Build time** — the Apollo Gradle plugin reads `SearchRepos.graphql` + `schema.graphqls`
 *    and generates `SearchReposQuery.kt` with fully typed data classes.
 *
 * 2. **Instantiation** — `SearchReposQuery(query = ..., after = ...)` creates a typed
 *    operation object; Apollo constructs the JSON request body from it.
 *
 * 3. **[Optional]** — nullable GraphQL variables (`$after: String`) become `Optional<String?>`.
 *    `Optional.present(null)` sends the variable as JSON `null`.
 *    `Optional.absent()` omits the variable from the request entirely.
 *
 * 4. **execute()** — suspend fun that performs the HTTP POST and returns
 *    `ApolloResponse<SearchReposQuery.Data>`. Network errors throw `ApolloException`.
 *
 * 5. **Union type access** — the `... on Repository` inline fragment in the query
 *    generates an `onRepository` property: it's non-null only for Repository nodes,
 *    null for User or Organization nodes. No manual type-checking needed.
 *
 * 6. **Scalars** — `URI` and `DateTime` custom scalars are mapped to `kotlin.String`
 *    via `mapScalarToKotlinString(...)` in `build.gradle.kts`.
 */
class ApolloSearchReposUseCase
    @Inject
    constructor(
        private val apolloClient: ApolloClient,
    ) {
        suspend operator fun invoke(
            query: String,
            token: String,
            after: String? = null,
        ): RepoSearchResult {
            val response =
                apolloClient
                    .query(
                        SearchReposQuery(
                            query = query,
                            // Optional.present(null) → variable sent as JSON null  (page 1: omit cursor)
                            // Optional.present("xyz") → variable sent as "xyz"     (page N: pass cursor)
                            // Using present() for both keeps the generated query signature simple.
                            after = Optional.present(after),
                        ),
                    )
                    // Token injected per-request so the ApolloClient itself is stateless and reusable.
                    .addHttpHeader("Authorization", "Bearer $token")
                    .execute() // suspend — throws ApolloException on network/protocol failure

            // GraphQL errors are NOT HTTP errors: the server returns HTTP 200 but sets
            // the "errors" array in the response body. We must check explicitly.
            if (!response.errors.isNullOrEmpty()) {
                error(response.errors!!.joinToString { it.message })
            }

            val search = checkNotNull(response.data?.search) { "Apollo: response contains no data" }

            val repos =
                search.edges
                    ?.mapNotNull { edge ->
                        // onRepository is generated from the `... on Repository` inline fragment.
                        // Returns null when the node is a User or Organization — mapNotNull skips them.
                        val repo = edge?.node?.onRepository ?: return@mapNotNull null
                        Repo(
                            name = repo.name,
                            nameWithOwner = repo.nameWithOwner,
                            description = repo.description,
                            stars = repo.stargazerCount,
                            language = repo.primaryLanguage?.name,
                            languageColor = repo.primaryLanguage?.color,
                            url = repo.url, // URI scalar → String (mapped in build.gradle.kts)
                            ownerLogin = repo.owner.login,
                            updatedAt = repo.updatedAt, // DateTime scalar → String
                        )
                    }.orEmpty()

            return RepoSearchResult(
                repos = repos,
                totalCount = search.repositoryCount,
                hasNextPage = search.pageInfo.hasNextPage,
                endCursor = search.pageInfo.endCursor,
            )
        }
    }
