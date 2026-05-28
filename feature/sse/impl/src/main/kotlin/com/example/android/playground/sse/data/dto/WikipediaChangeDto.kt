package com.example.android.playground.sse.data.dto

import com.example.android.playground.sse.domain.model.WikipediaChange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikipediaChangeDto(
    @SerialName("type") val type: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("user") val user: String = "",
    @SerialName("wiki") val wiki: String = "",
    @SerialName("comment") val comment: String = "",
    @SerialName("timestamp") val timestamp: Long = 0L,
) {
    fun toDomain(): WikipediaChange =
        WikipediaChange(
            type = type,
            title = title,
            user = user,
            wiki = wiki,
            comment = comment,
            timestampMs = timestamp * 1_000L,
        )
}
