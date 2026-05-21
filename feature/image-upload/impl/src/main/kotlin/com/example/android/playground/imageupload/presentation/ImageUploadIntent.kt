package com.example.android.playground.imageupload.presentation

sealed interface ImageUploadIntent {
    data object NavigationBack : ImageUploadIntent

    data object StartUpload : ImageUploadIntent

    data object ClearResults : ImageUploadIntent
}
