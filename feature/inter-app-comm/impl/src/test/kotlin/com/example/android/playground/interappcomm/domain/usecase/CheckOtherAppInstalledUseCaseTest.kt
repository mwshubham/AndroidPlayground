package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckOtherAppInstalledUseCaseTest {
    private val repository: InterAppCommRepository = mockk()
    private lateinit var useCase: CheckOtherAppInstalledUseCase

    @Before
    fun setUp() {
        useCase = CheckOtherAppInstalledUseCase(repository)
    }

    @Test
    fun `invoke returns true when other app is installed`() {
        every { repository.isOtherAppInstalled(any()) } returns true

        assertTrue(useCase("com.example.android.playground"))
        verify(exactly = 1) { repository.isOtherAppInstalled("com.example.android.playground") }
    }

    @Test
    fun `invoke returns false when other app is not installed`() {
        every { repository.isOtherAppInstalled(any()) } returns false

        assertFalse(useCase("com.example.android.playground"))
    }
}
