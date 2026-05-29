package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.Context
import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.domain.usecase.GetBroadcastMessagesUseCase
import com.example.android.playground.interappcomm.domain.usecase.SendBroadcastUseCase
import com.example.android.playground.interappcomm.presentation.intent.BroadcastIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.BroadcastSideEffect
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BroadcastViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = mockk(relaxed = true)
    private val sendBroadcast: SendBroadcastUseCase = mockk()
    private val getBroadcastMessages: GetBroadcastMessagesUseCase = mockk()
    private val store: InterAppMessageStore = mockk(relaxed = true)

    private val sentMessage =
        IpcMessage(
            content = "Hello",
            sender = "com.example.android.playground",
            method = IpcMethod.BROADCAST,
            direction = MessageDirection.SENT,
        )

    private fun createViewModel(): BroadcastViewModel {
        every { context.packageName } returns "com.example.android.playground"
        every { getBroadcastMessages() } returns MutableStateFlow(emptyList())
        return BroadcastViewModel(context, sendBroadcast, getBroadcastMessages, store)
    }

    @Test
    fun `OnInputChanged updates inputText in state`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(BroadcastIntent.OnInputChanged("Hello!"))

            viewModel.state.test {
                assertEquals("Hello!", awaitItem().inputText)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SendBroadcast with empty input does not send`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(BroadcastIntent.SendBroadcast)

            viewModel.state.test {
                assertTrue(awaitItem().inputText.isEmpty())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SendBroadcast with content sends message and clears inputText`() =
        runTest {
            every { sendBroadcast(any()) } returns sentMessage
            val viewModel = createViewModel()
            viewModel.handleIntent(BroadcastIntent.OnInputChanged("Hello!"))

            viewModel.handleIntent(BroadcastIntent.SendBroadcast)

            viewModel.state.test {
                assertEquals("", awaitItem().inputText)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ClearMessages delegates to store`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(BroadcastIntent.ClearMessages)

            verify { store.clearBroadcastMessages() }
        }

    @Test
    fun `NavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(BroadcastIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(BroadcastSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
