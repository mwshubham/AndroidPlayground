package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetIpcChannelsUseCaseTest {
    private val repository: InterAppCommRepository = mockk()
    private lateinit var useCase: GetIpcChannelsUseCase

    private val sampleChannel =
        IpcChannel(
            method = IpcMethod.BROADCAST,
            title = "Broadcast",
            tagline = "Send messages across apps",
            syncAsync = "Async",
            dataStyle = "Unstructured",
            securityLabel = "Signature",
            useCases = listOf("Event notifications"),
        )

    @Before
    fun setUp() {
        useCase = GetIpcChannelsUseCase(repository)
    }

    @Test
    fun `invoke returns channels from repository`() {
        every { repository.getIpcChannels() } returns listOf(sampleChannel)

        val result = useCase()

        assertEquals(listOf(sampleChannel), result)
    }

    @Test
    fun `invoke returns empty list when no channels`() {
        every { repository.getIpcChannels() } returns emptyList()

        assertEquals(emptyList<IpcChannel>(), useCase())
    }

    @Test
    fun `invoke delegates to repository exactly once`() {
        every { repository.getIpcChannels() } returns emptyList()

        useCase()

        verify(exactly = 1) { repository.getIpcChannels() }
    }
}
