package com.example.android.playground.imageupload.domain.repository

import com.example.android.playground.imageupload.presentation.ImageUploadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface ImageUploadStateRepository {
    val state: StateFlow<ImageUploadState>

    fun updateState(newState: ImageUploadState)

    fun clearState()

    fun getApplicationScope(): CoroutineScope
}
