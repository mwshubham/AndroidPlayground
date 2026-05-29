package com.example.android.playground.interappcomm.presentation.viewmodel

import android.content.Context
import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.usecase.CheckOtherAppInstalledUseCase
import com.example.android.playground.interappcomm.domain.usecase.GetAppSignatureUseCase
import com.example.android.playground.interappcomm.domain.usecase.GetIpcChannelsUseCase
import com.example.android.playground.interappcomm.presentation.intent.InterAppCommHomeIntent
import com.example.android.playground.interappcomm.presentation.sideeffect.InterAppCommHomeSideEffect
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class InterAppCommHomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = mockk(relaxed = true)
    private val checkOtherAppInstalled: CheckOtherAppInstalledUseCase = mockk()
    private val getAppSignature: GetAppSignatureUseCase = mockk()
    private val getIpcChannels: GetIpcChannelsUseCase = mockk()

    private val sampleChannel =
        IpcChannel(
            method = IpcMethod.BROADCAST,
            title = "Broadcast",
            tagline = "Send messages",
            syncAsync = "Async",
            dataStyle = "Unstructured",
            securityLabel = "Signature",
            useCases = emptyList(),
        )

    private fun createViewModel(): InterAppCommHomeViewModel {
        every { context.packageName } returns "com.example.android.playground"
        every { checkOtherAppInstalled(any()) } returns false
        every { getAppSignature(any()) } returns null
        every { getIpcChannels() } returns listOf(sampleChannel)
        return InterAppCommHomeViewModel(context, checkOtherAppInstalled, getAppSignature, getIpcChannels)
    }

    @Test
    fun `init loads data and sets ipcChannels`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertEquals(1, state.ipcChannels.size)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init sets isOtherAppInstalled to false when app not present`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                assertFalse(awaitItem().isOtherAppInstalled)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(InterAppCommHomeIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(InterAppCommHomeSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `OnChannelClicked emits NavigateToChannel with correct method`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(InterAppCommHomeIntent.OnChannelClicked(IpcMethod.BROADCAST))

            viewModel.sideEffect.test {
                val effect = awaitItem() as InterAppCommHomeSideEffect.NavigateToChannel
                assertEquals(IpcMethod.BROADCAST, effect.method)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init sets signaturesMatch true when both signatures match`() =
        runTest {
            every { context.packageName } returns "com.example.android.playground"
            every { checkOtherAppInstalled(any()) } returns true
            every { getAppSignature(any()) } returns "AA:BB:CC"
            every { getIpcChannels() } returns emptyList()
            val viewModel =
                InterAppCommHomeViewModel(
                    context,
                    checkOtherAppInstalled,
                    getAppSignature,
                    getIpcChannels,
                )

            viewModel.state.test {
                assertTrue(awaitItem().signaturesMatch)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
