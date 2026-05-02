package com.example.android.playground.feed.presentation.intent

sealed interface FeedIntent {
    data object LoadTopics : FeedIntent

    data object RefreshTopics : FeedIntent
}
