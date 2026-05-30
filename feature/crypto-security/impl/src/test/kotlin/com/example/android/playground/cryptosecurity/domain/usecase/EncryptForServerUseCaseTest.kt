package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EncryptForServerUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: EncryptForServerUseCase

    private val fakePayload =
        HybridEncryptedPayload(
            encryptedKey = byteArrayOf(1, 2),
            iv = byteArrayOf(3, 4),
            encryptedPayload = byteArrayOf(5, 6),
        )

    @Before
    fun setUp() {
        useCase = EncryptForServerUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns payload`() {
        every { repository.encryptForServer("hello") } returns fakePayload

        val result = useCase("hello")

        assertEquals(fakePayload, result)
        verify(exactly = 1) { repository.encryptForServer("hello") }
    }

    @Test
    fun `invoke propagates exception from repository`() {
        every { repository.encryptForServer(any()) } throws IllegalStateException("RSA key not ready")

        var thrown: Throwable? = null
        runCatching { useCase("msg") }.onFailure { thrown = it }

        assert(thrown is IllegalStateException)
    }
}
