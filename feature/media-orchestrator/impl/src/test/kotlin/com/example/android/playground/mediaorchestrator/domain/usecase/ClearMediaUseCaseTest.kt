package com.example.android.playground.mediaorchestrator.domain.usecase

import androidx.work.WorkManager
import com.example.android.playground.mediaorchestrator.domain.repository.MediaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearMediaUseCaseTest {
    private val repository: MediaRepository = mockk()
    private val workManager: WorkManager = mockk()
    private lateinit var useCase: ClearMediaUseCase

    @Before
    fun setUp() {
        useCase = ClearMediaUseCase(repository, workManager)
    }

    @Test
    fun `invoke cancels unique work and clears repository`() =
        runTest {
            every { workManager.cancelUniqueWork(any()) } returns mockk()
            coEvery { repository.clearAll() } just runs

            useCase()

            verify(exactly = 1) { workManager.cancelUniqueWork(any()) }
            coVerify(exactly = 1) { repository.clearAll() }
        }
}
