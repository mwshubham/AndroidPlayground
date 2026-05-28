package com.example.android.playground.imageupload.domain.usecase

import com.example.android.playground.imageupload.domain.model.ImageUploadResult
import com.example.android.playground.imageupload.domain.model.UploadStatus
import com.example.android.playground.imageupload.domain.repository.ImageUploadRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UploadMultipleImagesUseCaseTest {
    private val repository: ImageUploadRepository = mockk()
    private lateinit var useCase: UploadMultipleImagesUseCase

    @Before
    fun setUp() {
        useCase = UploadMultipleImagesUseCase(repository)
    }

    @Test
    fun `invoke with count 3 calls uploadImage 3 times and emits 3 results`() =
        runTest {
            val successResult = ImageUploadResult(id = "img_001", url = "https://cdn/img", status = UploadStatus.SUCCESS)
            coEvery { repository.uploadImage(any()) } returns successResult

            val results = useCase(3).toList()

            assertEquals(3, results.size)
            results.forEach { assertEquals(UploadStatus.SUCCESS, it.status) }
            coVerify(exactly = 3) { repository.uploadImage(any()) }
        }

    @Test
    fun `invoke with count 0 emits no results`() =
        runTest {
            val results = useCase(0).toList()

            assertEquals(0, results.size)
        }

    @Test
    fun `invoke propagates failure result from repository`() =
        runTest {
            val failureResult =
                ImageUploadResult(
                    id = "img_fail",
                    url = "",
                    status = UploadStatus.FAILURE,
                    error = "Server error",
                )
            coEvery { repository.uploadImage(any()) } returns failureResult

            val results = useCase(1).toList()

            assertEquals(1, results.size)
            assertEquals(UploadStatus.FAILURE, results[0].status)
        }
}
