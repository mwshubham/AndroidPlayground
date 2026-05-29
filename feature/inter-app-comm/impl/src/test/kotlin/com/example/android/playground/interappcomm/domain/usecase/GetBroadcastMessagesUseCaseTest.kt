package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetBroadcastMessagesUseCaseTest {
    private val store: InterAppMessageStore = mockk()
    private lateinit var useCase: GetBroadcastMessagesUseCase

    private val sampleMessage =
        IpcMessage(
            content = "Hello",
            sender = "com.example.app",
            method = IpcMethod.BROADCAST,
            direction = MessageDirection.RECEIVED,
        )

    @Before
    fun setUp() {
        useCase = GetBroadcastMessagesUseCase(store)
    }

    @Test
    fun `invoke returns broadcastMessages StateFlow from store`() {
        val flow = MutableStateFlow(listOf(sampleMessage))
        every { store.broadcastMessages } returns flow

        val result = useCase()

        assertEquals(flow, result)
    }

    @Test
    fun `invoke returns empty flow when no messages`() {
        val flow = MutableStateFlow(emptyList<IpcMessage>())
        every { store.broadcastMessages } returns flow

        val result = useCase()

        assertEquals(0, result.value.size)
    }

    @Test
    fun `invoke delegates to store exactly once`() {
        every { store.broadcastMessages } returns MutableStateFlow(emptyList())

        useCase()

        verify(exactly = 1) { store.broadcastMessages }
    }
}
