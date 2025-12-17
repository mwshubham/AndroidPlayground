package com.example.android.playground.imageupload.presentation

sealed class ImageUploadIntent {
    object NavigationBack : ImageUploadIntent()
    object StartUpload : ImageUploadIntent()
    object ClearResults : ImageUploadIntent()
}
