package com.example.android.systemdesign.feed.domain.usecase

import com.example.android.systemdesign.feed.domain.model.Topic
import com.example.android.systemdesign.feed.domain.repository.FeedRepository
import javax.inject.Inject

class GetFeedTopicsUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(): List<Topic> {
        return feedRepository.getTopics()
    }
}
