package com.example.android.playground.graphql.presentation.sideeffect

sealed interface GitHubExplorerSideEffect {
    data class OpenUrl(
        val url: String,
    ) : GitHubExplorerSideEffect

    data class ShowMessage(
        val message: String,
    ) : GitHubExplorerSideEffect

    data object NavigateBack : GitHubExplorerSideEffect
}
