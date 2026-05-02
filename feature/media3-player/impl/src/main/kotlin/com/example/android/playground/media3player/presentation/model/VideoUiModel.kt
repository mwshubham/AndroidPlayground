package com.example.android.playground.media3player.presentation.model

data class VideoUiModel(
    val id: String,
    val title: String,
    val dashUrl: String,
    val drmLicenseUrl: String?,
    val description: String,
)
