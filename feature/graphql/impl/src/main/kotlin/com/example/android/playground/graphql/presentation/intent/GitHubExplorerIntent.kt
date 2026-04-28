package com.example.android.playground.graphql.presentation.intent

import com.example.android.playground.graphql.presentation.model.DataSourceMode

sealed interface GitHubExplorerIntent {
    data class OnModeChanged(val mode: DataSourceMode) : GitHubExplorerIntent
    data class OnTokenChanged(val token: String) : GitHubExplorerIntent
    data object OnSaveToken : GitHubExplorerIntent
    data object OnClearToken : GitHubExplorerIntent
    data class OnSearchQueryChanged(val query: String) : GitHubExplorerIntent
    data object OnSearch : GitHubExplorerIntent
    data object OnLoadMore : GitHubExplorerIntent
    data class OnRepoClicked(val url: String) : GitHubExplorerIntent
    data object OnDismissError : GitHubExplorerIntent
    data object NavigateBack : GitHubExplorerIntent
}
