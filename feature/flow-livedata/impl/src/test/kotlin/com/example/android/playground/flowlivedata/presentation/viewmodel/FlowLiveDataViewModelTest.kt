package com.example.android.playground.flowlivedata.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.flowlivedata.presentation.intent.FlowLiveDataIntent
import com.example.android.playground.flowlivedata.presentation.model.StreamType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class FlowLiveDataViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel() = FlowLiveDataViewModel()

    @Test
    fun `initial state defaults to STATE_FLOW tab`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(StreamType.STATE_FLOW, state.selectedTab)
                assertEquals(0, state.stateFlowValue)
                assertFalse(state.isEmitting)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SelectTab intent changes selected tab`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(FlowLiveDataIntent.SelectTab(StreamType.SHARED_FLOW))

            viewModel.state.test {
                assertEquals(StreamType.SHARED_FLOW, awaitItem().selectedTab)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ToggleEmitting flips isEmitting to true when idle`() =
        runTest {
            val viewModel = createViewModel()
            assertFalse(viewModel.state.value.isEmitting)

            viewModel.handleIntent(FlowLiveDataIntent.ToggleEmitting)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals(true, state.isEmitting)
                cancelAndIgnoreRemainingEvents()
            }

            // Cancel the infinite emitJob before runTest's advanceUntilIdle drains it forever
            viewModel.handleIntent(FlowLiveDataIntent.ToggleEmitting)
        }
}
