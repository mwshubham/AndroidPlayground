package com.example.android.playground.imageupload.presentation

import androidx.lifecycle.ViewModel
import com.example.android.playground.common.AppConstants
import com.example.android.playground.imageupload.domain.model.ImageUploadResult
import com.example.android.playground.imageupload.domain.model.UploadStatus
import com.example.android.playground.imageupload.domain.repository.ImageUploadStateRepository
import com.example.android.playground.imageupload.domain.usecase.UploadMultipleImagesUseCase
import com.example.android.playground.imageupload.util.ImageUploadConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    private val uploadMultipleImagesUseCase: UploadMultipleImagesUseCase,
    private val stateRepository: ImageUploadStateRepository
) : ViewModel() {

    val state: StateFlow<ImageUploadState> = stateRepository.state

    fun handleIntent(intent: ImageUploadIntent) {
        when (intent) {
            is ImageUploadIntent.StartUpload -> startUpload()
            is ImageUploadIntent.ClearResults -> clearResults()
            else -> {}
        }
    }

    private fun startUpload() {
        if (state.value.isUploading) return

        stateRepository.updateState(
            state.value.copy(
                isUploading = true,
                uploadResults = emptyList(),
                successCount = 0,
                failureCount = 0,
                totalCount = ImageUploadConstants.Upload.DEFAULT_UPLOAD_COUNT,
                progress = 0f
            )
        )

        // Use application-scoped coroutine instead of viewModelScope
        // This ensures uploads continue even if the ViewModel is destroyed
        stateRepository.getApplicationScope().launch {
            // Simulate initial delay before starting uploads
            delay(AppConstants.DEFAULT_DELAY)
            uploadMultipleImagesUseCase(ImageUploadConstants.Upload.DEFAULT_UPLOAD_COUNT)
                .catch { _ ->
                    // Handle any unexpected errors
                    stateRepository.updateState(
                        state.value.copy(isUploading = false)
                    )
                }
                .collect { result ->
                    updateUploadResult(result)
                }
        }
    }

    private fun updateUploadResult(result: ImageUploadResult) {
        val currentState = state.value
        val updatedResults = currentState.uploadResults + result
        val newSuccessCount = updatedResults.count { it.status == UploadStatus.SUCCESS }
        val newFailureCount = updatedResults.count { it.status == UploadStatus.FAILURE }
        val newProgress = updatedResults.size.toFloat() / currentState.totalCount

        stateRepository.updateState(
            currentState.copy(
                uploadResults = updatedResults,
                successCount = newSuccessCount,
                failureCount = newFailureCount,
                progress = newProgress,
                isUploading = updatedResults.size < currentState.totalCount
            )
        )
    }

    private fun clearResults() {
        stateRepository.clearState()
    }
}
