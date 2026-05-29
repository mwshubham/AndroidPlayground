package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetAppSignatureUseCaseTest {
    private val repository: InterAppCommRepository = mockk()
    private lateinit var useCase: GetAppSignatureUseCase

    @Before
    fun setUp() {
        useCase = GetAppSignatureUseCase(repository)
    }

    @Test
    fun `invoke returns fingerprint from repository`() {
        every { repository.getAppSignatureFingerprint(any()) } returns "AB:CD:EF"

        val result = useCase("com.example.android.playground")

        assertEquals("AB:CD:EF", result)
        verify(exactly = 1) { repository.getAppSignatureFingerprint("com.example.android.playground") }
    }

    @Test
    fun `invoke returns null when fingerprint is unavailable`() {
        every { repository.getAppSignatureFingerprint(any()) } returns null

        assertNull(useCase("com.unknown.package"))
    }
}
