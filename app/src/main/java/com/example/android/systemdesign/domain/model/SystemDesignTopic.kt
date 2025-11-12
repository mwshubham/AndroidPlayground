package com.example.android.systemdesign.domain.model

import androidx.annotation.StringRes

data class SystemDesignTopic(
    val id: SystemDesignTopicId,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int
)
