package com.example.android.playground.feed.presentation.sideeffect

sealed interface FeedSideEffect {
    data class ShowError(
        val message: String,
    ) : FeedSideEffect

    data object TopicsRefreshed : FeedSideEffect
}
