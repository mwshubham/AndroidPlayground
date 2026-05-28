package com.example.android.playground.grpc.domain.usecase

import com.example.android.playground.grpc.domain.repository.GrpcRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SendElizaMessageUseCaseTest {
    private val repository: GrpcRepository = mockk()
    private lateinit var useCase: SendElizaMessageUseCase

    @Before
    fun setUp() {
        useCase = SendElizaMessageUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns reply`() =
        runTest {
            coEvery { repository.sendMessage("hello") } returns "How can I help?"

            val result = useCase("hello")

            assertEquals("How can I help?", result)
            coVerify(exactly = 1) { repository.sendMessage("hello") }
        }
}
