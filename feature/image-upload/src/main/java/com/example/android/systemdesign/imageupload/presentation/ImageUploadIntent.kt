package com.example.android.systemdesign.imageupload.presentation

sealed class ImageUploadIntent {
    object NavigationBack : ImageUploadIntent()
    object StartUpload : ImageUploadIntent()
    object ClearResults : ImageUploadIntent()
}
