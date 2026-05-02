package com.example.android.playground.graphql.data.network

import com.example.android.playground.graphql.data.network.dto.GraphQLRequest
import com.example.android.playground.graphql.data.network.dto.SearchReposResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

private const val GITHUB_GRAPHQL_URL = "https://api.github.com/graphql"
private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

/**
 * GraphQL is transport-agnostic but conventionally uses HTTP POST.
 * Every request sends a JSON body with two fields:
 *   - "query"     — the GraphQL document (operations + fragments)
 *   - "variables" — a map of variable name → value used to parameterise the query
 *
 * The server responds with a JSON object that always has:
 *   - "data"   — the query result (null on error)
 *   - "errors" — list of GraphQL-level errors (partial failures are possible)
 */
internal class GitHubGraphQLService
    @Inject
    constructor(
        private val okHttpClient: OkHttpClient,
        private val json: Json,
    ) {
        /**
         * Executes the GitHub repository search query.
         *
         * Query demonstrates three core GraphQL concepts:
         * 1. **Named operation** — `query SearchRepos` for debuggability
         * 2. **Variables** — `$query` and `$after` avoid string interpolation
         * 3. **Inline fragments** — `... on Repository` handles union types
         */
        suspend fun searchRepos(
            query: String,
            token: String,
            after: String?,
        ): SearchReposResponse {
            val variables =
                buildMap {
                    put("query", query)
                    put("after", after)
                }

            val requestBody =
                json
                    .encodeToString(
                        GraphQLRequest(
                            query = SEARCH_REPOS_QUERY,
                            variables = variables,
                        ),
                    ).toRequestBody(JSON_MEDIA_TYPE)

            val request =
                Request
                    .Builder()
                    .url(GITHUB_GRAPHQL_URL)
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .post(requestBody)
                    .build()

            // OkHttp's execute() is a blocking call — run it on the IO dispatcher
            // so the main thread stays free to render the isLoading indicator.
            val responseBody =
                withContext(Dispatchers.IO) {
                    okHttpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            error("HTTP ${response.code}: ${response.message}")
                        }
                        checkNotNull(response.body).string()
                    }
                }

            return withContext(Dispatchers.Default) {
                json.decodeFromString(responseBody)
            }
        }
    }

// ---------------------------------------------------------------------------
// GraphQL document
// ---------------------------------------------------------------------------

/**
 * Searches GitHub repositories and returns paginated results.
 *
 * Concepts demonstrated:
 *  - Aliases           : `search` field is the root query alias
 *  - Arguments         : `type: REPOSITORY`, `first: 20`, `after: $after`
 *  - Pagination        : cursor-based via `pageInfo { endCursor hasNextPage }`
 *  - Inline fragments  : `... on Repository` — the `search` union can return
 *                        User/Organization/Repository nodes
 *  - Scalar fields     : `stargazerCount`, `updatedAt`, `url`
 *  - Nested objects    : `primaryLanguage`, `owner`
 */
private val SEARCH_REPOS_QUERY =
    """
    query SearchRepos(${'$'}query: String!, ${'$'}after: String) {
      search(query: ${'$'}query, type: REPOSITORY, first: 20, after: ${'$'}after) {
        repositoryCount
        pageInfo {
          endCursor
          hasNextPage
        }
        edges {
          node {
            ... on Repository {
              name
              nameWithOwner
              description
              stargazerCount
              primaryLanguage {
                name
                color
              }
              url
              owner {
                login
                avatarUrl
              }
              updatedAt
            }
          }
        }
      }
    }
    """.trimIndent()
