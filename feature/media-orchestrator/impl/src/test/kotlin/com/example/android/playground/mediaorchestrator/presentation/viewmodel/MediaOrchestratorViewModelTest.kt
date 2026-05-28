package com.example.android.playground.mediaorchestrator.presentation.viewmodel

import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.mediaorchestrator.domain.usecase.AddMediaItemsUseCase
import com.example.android.playground.mediaorchestrator.domain.usecase.ClearMediaUseCase
import com.example.android.playground.mediaorchestrator.domain.usecase.EnqueueOrchestratorUseCase
import com.example.android.playground.mediaorchestrator.domain.usecase.GetMediaItemsUseCase
import com.example.android.playground.mediaorchestrator.presentation.intent.MediaOrchestratorIntent
import com.example.android.playground.mediaorchestrator.presentation.sideeffect.MediaOrchestratorSideEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MediaOrchestratorViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMediaItemsUseCase: GetMediaItemsUseCase = mockk()
    private val addMediaItemsUseCase: AddMediaItemsUseCase = mockk()
    private val enqueueOrchestratorUseCase: EnqueueOrchestratorUseCase = mockk()
    private val clearMediaUseCase: ClearMediaUseCase = mockk()
    private val workManager: WorkManager = mockk()

    private fun createViewModel(): MediaOrchestratorViewModel {
        every { getMediaItemsUseCase() } returns flowOf(emptyList())
        every { workManager.getWorkInfosForUniqueWorkFlow(any()) } returns flowOf(emptyList<WorkInfo>())
        return MediaOrchestratorViewModel(
            getMediaItemsUseCase,
            addMediaItemsUseCase,
            enqueueOrchestratorUseCase,
            clearMediaUseCase,
            workManager,
        )
    }

    @Test
    fun `init starts with empty items and idle worker status`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(state.items.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ClearAll intent calls clearMediaUseCase and emits ShowMessage`() =
        runTest {
            coEvery { clearMediaUseCase() } just runs
            val viewModel = createViewModel()

            viewModel.handleIntent(MediaOrchestratorIntent.ClearAll)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is MediaOrchestratorSideEffect.ShowMessage)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { clearMediaUseCase() }
        }

    @Test
    fun `NavigateBack intent emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MediaOrchestratorIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(MediaOrchestratorSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
