package com.example.android.playground.graphql.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GraphQLRequest(
    val query: String,
    val variables: Map<String, String?> = emptyMap(),
)
