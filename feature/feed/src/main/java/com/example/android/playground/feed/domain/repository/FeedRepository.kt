package com.example.android.playground.feed.domain.repository

import com.example.android.playground.feed.domain.model.Topic

interface FeedRepository {
    suspend fun getTopics(): List<Topic>
}
