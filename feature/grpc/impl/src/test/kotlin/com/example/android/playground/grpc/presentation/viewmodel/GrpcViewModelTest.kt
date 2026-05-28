package com.example.android.playground.grpc.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.grpc.domain.model.MessageRole
import com.example.android.playground.grpc.domain.usecase.SendElizaMessageUseCase
import com.example.android.playground.grpc.presentation.intent.GrpcIntent
import com.example.android.playground.grpc.presentation.sideeffect.GrpcSideEffect
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GrpcViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sendElizaMessageUseCase: SendElizaMessageUseCase = mockk()

    private fun createViewModel() = GrpcViewModel(sendElizaMessageUseCase)

    @Test
    fun `initial state has empty messages and not loading`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(state.messages.isEmpty())
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `UpdateInput intent updates inputText`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.processIntent(GrpcIntent.UpdateInput("Hi there"))

            viewModel.state.test {
                assertEquals("Hi there", awaitItem().inputText)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SendMessage intent adds user message and appends eliza reply`() =
        runTest {
            coEvery { sendElizaMessageUseCase("hello") } returns "Tell me more."
            val viewModel = createViewModel()

            viewModel.processIntent(GrpcIntent.SendMessage("hello"))

            viewModel.state.test {
                val state = awaitItem()
                assertTrue(state.messages.any { it.role == MessageRole.USER && it.text == "hello" })
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SendMessage emits ScrollToBottom side effect`() =
        runTest {
            coEvery { sendElizaMessageUseCase(any()) } returns "Response"
            val viewModel = createViewModel()

            viewModel.sideEffects.test {
                viewModel.processIntent(GrpcIntent.SendMessage("test"))
                assertEquals(GrpcSideEffect.ScrollToBottom, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}
