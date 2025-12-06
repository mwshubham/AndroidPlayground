package com.example.android.systemdesign.feed.domain.repository

import com.example.android.systemdesign.feed.domain.model.Topic

interface FeedRepository {
    suspend fun getTopics(): List<Topic>
}
