package com.example.android.systemdesign.imageupload.data.repository

import com.example.android.systemdesign.imageupload.domain.repository.ImageUploadStateRepository
import com.example.android.systemdesign.imageupload.presentation.ImageUploadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUploadStateRepositoryImpl @Inject constructor() : ImageUploadStateRepository {

    // Application-scoped coroutine that survives beyond composable lifecycle
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(ImageUploadState())
    override val state: StateFlow<ImageUploadState> = _state.asStateFlow()

    override fun updateState(newState: ImageUploadState) {
        _state.value = newState
    }

    override fun clearState() {
        _state.value = ImageUploadState()
    }

    override fun getApplicationScope(): CoroutineScope = applicationScope
}
