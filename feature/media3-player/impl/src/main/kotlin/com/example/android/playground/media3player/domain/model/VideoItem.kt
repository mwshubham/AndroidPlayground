package com.example.android.playground.media3player.domain.model

data class VideoItem(
    val id: String,
    val title: String,
    val dashUrl: String,
    val drmLicenseUrl: String?,
    val description: String,
)
