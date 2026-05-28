package com.example.android.playground.imageupload.presentation

import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.imageupload.domain.repository.ImageUploadStateRepository
import com.example.android.playground.imageupload.domain.usecase.UploadMultipleImagesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ImageUploadViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val uploadMultipleImagesUseCase: UploadMultipleImagesUseCase = mockk()
    private val stateRepository: ImageUploadStateRepository = mockk(relaxed = true)

    private val stateFlow = MutableStateFlow(ImageUploadState())

    private fun createViewModel(): ImageUploadViewModel {
        every { stateRepository.state } returns stateFlow
        every { stateRepository.getApplicationScope() } returns CoroutineScope(mainDispatcherRule.testDispatcher)
        return ImageUploadViewModel(uploadMultipleImagesUseCase, stateRepository)
    }

    @Test
    fun `state is sourced from stateRepository`() =
        runTest {
            val viewModel = createViewModel()

            // The ViewModel exposes exactly what the repository provides.
            assert(viewModel.state === stateFlow)
        }

    @Test
    fun `ClearResults intent calls stateRepository clearState`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(ImageUploadIntent.ClearResults)

            verify(exactly = 1) { stateRepository.clearState() }
        }

    @Test
    fun `StartUpload intent when not uploading calls stateRepository updateState with isUploading true`() =
        runTest {
            stateFlow.value = ImageUploadState(isUploading = false)
            val viewModel = createViewModel()

            viewModel.handleIntent(ImageUploadIntent.StartUpload)

            // First updateState call must set isUploading = true
            verify {
                stateRepository.updateState(
                    match { it.isUploading },
                )
            }
        }

    @Test
    fun `StartUpload intent is no-op when already uploading`() =
        runTest {
            stateFlow.value = ImageUploadState(isUploading = true)
            val viewModel = createViewModel()

            viewModel.handleIntent(ImageUploadIntent.StartUpload)

            // updateState must NOT be called when upload is already in progress
            verify(exactly = 0) { stateRepository.updateState(any()) }
        }
}
