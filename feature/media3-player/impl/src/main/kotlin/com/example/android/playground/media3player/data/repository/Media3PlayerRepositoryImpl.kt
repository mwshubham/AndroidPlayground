package com.example.android.playground.media3player.data.repository

import com.example.android.playground.media3player.domain.model.VideoItem
import com.example.android.playground.media3player.domain.repository.Media3PlayerRepository
import javax.inject.Inject

class Media3PlayerRepositoryImpl
    @Inject
    constructor() : Media3PlayerRepository {
        override suspend fun getVideos(): List<VideoItem> =
            listOf(
                VideoItem(
                    id = "angel_one_widevine",
                    title = "Angel One (Widevine DRM)",
                    dashUrl = "https://storage.googleapis.com/shaka-demo-assets/angel-one-widevine/dash.mpd",
                    drmLicenseUrl = "https://cwip-shaka-proxy.appspot.com/no_auth",
                    description = "Short animated film encoded with Widevine DRM. Uses DASH adaptive streaming.",
                ),
                VideoItem(
                    id = "sintel_widevine",
                    title = "Sintel (Widevine DRM)",
                    dashUrl = "https://storage.googleapis.com/shaka-demo-assets/sintel-widevine/dash.mpd",
                    drmLicenseUrl = "https://cwip-shaka-proxy.appspot.com/no_auth",
                    description = "Blender open-movie with Widevine DRM. Demonstrates multi-language audio and subtitles.",
                ),
                VideoItem(
                    id = "tears_of_steel",
                    title = "Tears of Steel (Clear DASH)",
                    dashUrl = "https://storage.googleapis.com/shaka-demo-assets/tears_of_steel/dash.mpd",
                    drmLicenseUrl = null,
                    description = "Blender open-movie in unencrypted DASH. Good for testing playback without DRM.",
                ),
            )
    }
