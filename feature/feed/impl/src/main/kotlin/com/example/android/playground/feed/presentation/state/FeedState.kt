package com.example.android.playground.feed.presentation.state

import com.example.android.playground.feed.domain.model.Topic

data class FeedState(
    val topics: List<Topic> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)
