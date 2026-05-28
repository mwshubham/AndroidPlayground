package com.example.android.playground.deviceclassifier.domain.usecase

import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import com.example.android.playground.deviceclassifier.domain.repository.DeviceClassifierRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetDeviceSpecUseCaseTest {
    private val repository: DeviceClassifierRepository = mockk()
    private lateinit var useCase: GetDeviceSpecUseCase

    @Before
    fun setUp() {
        useCase = GetDeviceSpecUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns its result`() =
        runTest {
            val expected = DeviceSpec(ramMb = 4_096L, cpuCores = 6, apiLevel = 33)
            coEvery { repository.getDeviceSpec() } returns expected

            val actual = useCase()

            assertEquals(expected, actual)
            coVerify(exactly = 1) { repository.getDeviceSpec() }
        }
}
