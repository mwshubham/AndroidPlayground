package com.example.android.systemdesign.presentation.main

import com.example.android.systemdesign.domain.model.SystemDesignTopic

// MVI State
data class MainState(
    val topics: List<SystemDesignTopic> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// MVI Intent
sealed class MainIntent {
    object LoadTopics : MainIntent()
}
