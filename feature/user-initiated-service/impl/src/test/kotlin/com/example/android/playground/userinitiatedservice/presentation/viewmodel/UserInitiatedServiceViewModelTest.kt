package com.example.android.playground.userinitiatedservice.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.userinitiatedservice.domain.model.TransferItem
import com.example.android.playground.userinitiatedservice.domain.model.TransferStatus
import com.example.android.playground.userinitiatedservice.domain.usecase.AddTransferItemsUseCase
import com.example.android.playground.userinitiatedservice.domain.usecase.ClearTransfersUseCase
import com.example.android.playground.userinitiatedservice.domain.usecase.GetTransferItemsUseCase
import com.example.android.playground.userinitiatedservice.domain.usecase.StartTransferUseCase
import com.example.android.playground.userinitiatedservice.presentation.intent.UserInitiatedServiceIntent
import com.example.android.playground.userinitiatedservice.presentation.sideeffect.UserInitiatedServiceSideEffect
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

class UserInitiatedServiceViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTransferItemsUseCase: GetTransferItemsUseCase = mockk()
    private val addTransferItemsUseCase: AddTransferItemsUseCase = mockk()
    private val startTransferUseCase: StartTransferUseCase = mockk()
    private val clearTransfersUseCase: ClearTransfersUseCase = mockk()

    private fun createViewModel(): UserInitiatedServiceViewModel {
        every { getTransferItemsUseCase() } returns flowOf(emptyList())
        return UserInitiatedServiceViewModel(
            getTransferItemsUseCase,
            addTransferItemsUseCase,
            startTransferUseCase,
            clearTransfersUseCase,
        )
    }

    @Test
    fun `init state has empty items and IDLE job status`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(state.items.isEmpty())
                assertEquals(
                    com.example.android.playground.userinitiatedservice.presentation.state.JobStatus.IDLE,
                    state.jobStatus,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `running items cause derived status to be RUNNING`() =
        runTest {
            val runningItem =
                TransferItem(
                    id = "1",
                    name = "file.zip",
                    sizeBytes = 1000L,
                    status = TransferStatus.RUNNING,
                    totalChunks = 10,
                    uploadedChunks = 3,
                    createdAt = 0L,
                )
            every { getTransferItemsUseCase() } returns flowOf(listOf(runningItem))
            val viewModel =
                UserInitiatedServiceViewModel(
                    getTransferItemsUseCase,
                    addTransferItemsUseCase,
                    startTransferUseCase,
                    clearTransfersUseCase,
                )

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(
                    com.example.android.playground.userinitiatedservice.presentation.state.JobStatus.RUNNING,
                    state.jobStatus,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ClearAll intent clears state and emits ShowMessage`() =
        runTest {
            coEvery { clearTransfersUseCase() } just runs
            val viewModel = createViewModel()

            viewModel.handleIntent(UserInitiatedServiceIntent.ClearAll)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is UserInitiatedServiceSideEffect.ShowMessage)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { clearTransfersUseCase() }
        }

    @Test
    fun `NavigateBack intent emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(UserInitiatedServiceIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(UserInitiatedServiceSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
