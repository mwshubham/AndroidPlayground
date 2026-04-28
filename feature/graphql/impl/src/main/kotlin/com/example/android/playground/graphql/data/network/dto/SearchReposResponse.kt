package com.example.android.playground.graphql.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchReposResponse(
    val data: SearchData? = null,
    val errors: List<GraphQLError>? = null,
)

@Serializable
data class SearchData(
    val search: SearchResult,
)

@Serializable
data class SearchResult(
    val repositoryCount: Int = 0,
    val pageInfo: PageInfoDto,
    val edges: List<RepositoryEdge> = emptyList(),
)

@Serializable
data class PageInfoDto(
    val endCursor: String? = null,
    val hasNextPage: Boolean = false,
)

@Serializable
data class RepositoryEdge(
    val node: RepositoryNode = RepositoryNode(),
)

@Serializable
data class RepositoryNode(
    val name: String = "",
    val nameWithOwner: String = "",
    val description: String? = null,
    val stargazerCount: Int = 0,
    val primaryLanguage: LanguageDto? = null,
    val url: String = "",
    val owner: OwnerDto? = null,
    val updatedAt: String = "",
)

@Serializable
data class LanguageDto(
    val name: String = "",
    val color: String? = null,
)

@Serializable
data class OwnerDto(
    val login: String = "",
    @SerialName("avatarUrl") val avatarUrl: String? = null,
)

@Serializable
data class GraphQLError(
    val message: String = "",
)
