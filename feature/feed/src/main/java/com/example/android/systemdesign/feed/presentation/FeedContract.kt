package com.example.android.systemdesign.feed.presentation

import com.example.android.systemdesign.feed.domain.model.Topic

// MVI State
data class FeedState(
    val topics: List<Topic> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

// MVI Intent
sealed class FeedIntent {
    object LoadTopics : FeedIntent()
    object RefreshTopics : FeedIntent()
}

// Side Effects (One-time events)
sealed class FeedSideEffect {
    data class ShowError(val message: String) : FeedSideEffect()
    object TopicsRefreshed : FeedSideEffect()
}
