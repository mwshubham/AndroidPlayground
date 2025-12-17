package com.example.android.playground.feed.domain.model

import androidx.annotation.StringRes

data class Topic(
    val id: TopicId,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int
)
