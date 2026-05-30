package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SimulateServerDecryptUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: SimulateServerDecryptUseCase

    private val fakePayload =
        HybridEncryptedPayload(
            encryptedKey = byteArrayOf(10),
            iv = byteArrayOf(20),
            encryptedPayload = byteArrayOf(30),
        )

    @Before
    fun setUp() {
        useCase = SimulateServerDecryptUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns decrypted plaintext`() {
        every { repository.simulateServerDecrypt(fakePayload) } returns "decryptedMessage"

        val result = useCase(fakePayload)

        assertEquals("decryptedMessage", result)
        verify(exactly = 1) { repository.simulateServerDecrypt(fakePayload) }
    }

    @Test
    fun `invoke propagates exception from repository`() {
        every { repository.simulateServerDecrypt(any()) } throws IllegalStateException("Private key error")

        var thrown: Throwable? = null
        runCatching { useCase(fakePayload) }.onFailure { thrown = it }

        assert(thrown is IllegalStateException)
    }
}
