package com.example.android.playground.graphql.presentation.state

import com.example.android.playground.graphql.presentation.model.DataSourceMode
import com.example.android.playground.graphql.presentation.model.RepoUiModel

data class GitHubExplorerState(
    val mode: DataSourceMode = DataSourceMode.RAW_OKHTTP,
    val token: String = "",
    val isTokenSaved: Boolean = false,
    val searchQuery: String = "",
    val repos: List<RepoUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val totalCount: Int = 0,
    val hasNextPage: Boolean = false,
    val endCursor: String? = null,
)
