package com.example.android.playground.feed.domain.usecase

import com.example.android.playground.feed.domain.model.Topic
import com.example.android.playground.feed.domain.repository.FeedRepository
import javax.inject.Inject

class GetFeedTopicsUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(): List<Topic> {
        return feedRepository.getTopics()
    }
}
