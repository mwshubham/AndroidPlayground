package com.example.android.systemdesign.imageupload.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.systemdesign.imageupload.domain.model.ImageUploadResult
import com.example.android.systemdesign.imageupload.domain.model.UploadStatus
import com.example.android.systemdesign.imageupload.domain.usecase.UploadMultipleImagesUseCase
import com.example.android.systemdesign.imageupload.util.ImageUploadConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    private val uploadMultipleImagesUseCase: UploadMultipleImagesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ImageUploadState())
    val state: StateFlow<ImageUploadState> = _state.asStateFlow()

    fun handleIntent(intent: ImageUploadIntent) {
        when (intent) {
            is ImageUploadIntent.StartUpload -> startUpload()
            is ImageUploadIntent.ClearResults -> clearResults()
            else -> {}
        }
    }

    private fun startUpload() {
        if (_state.value.isUploading) return

        _state.value = _state.value.copy(
            isUploading = true,
            uploadResults = emptyList(),
            successCount = 0,
            failureCount = 0,
            totalCount = ImageUploadConstants.Upload.DEFAULT_UPLOAD_COUNT,
            progress = 0f
        )

        viewModelScope.launch {
            // Simulate initial delay before starting uploads
            delay(2_000)
            uploadMultipleImagesUseCase(ImageUploadConstants.Upload.DEFAULT_UPLOAD_COUNT)
                .catch { _ ->
                    // Handle any unexpected errors
                    _state.value = _state.value.copy(
                        isUploading = false
                    )
                }
                .collect { result ->
                    updateUploadResult(result)
                }
        }
    }

    private fun updateUploadResult(result: ImageUploadResult) {
        val currentState = _state.value
        val updatedResults = currentState.uploadResults + result
        val newSuccessCount = updatedResults.count { it.status == UploadStatus.SUCCESS }
        val newFailureCount = updatedResults.count { it.status == UploadStatus.FAILURE }
        val newProgress = updatedResults.size.toFloat() / currentState.totalCount

        _state.value = currentState.copy(
            uploadResults = updatedResults,
            successCount = newSuccessCount,
            failureCount = newFailureCount,
            progress = newProgress,
            isUploading = updatedResults.size < currentState.totalCount
        )
    }

    private fun clearResults() {
        _state.value = ImageUploadState()
    }
}
