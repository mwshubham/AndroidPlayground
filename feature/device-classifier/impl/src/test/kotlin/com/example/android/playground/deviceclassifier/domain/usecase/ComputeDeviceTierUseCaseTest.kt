package com.example.android.playground.deviceclassifier.domain.usecase

import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ComputeDeviceTierUseCaseTest {
    private lateinit var useCase: ComputeDeviceTierUseCase

    @Before
    fun setUp() {
        useCase = ComputeDeviceTierUseCase()
    }

    // --- Happy path ---

    @Test
    fun `low ram and low cpu cores returns LOW tier`() {
        assertEquals(DeviceTier.LOW, useCase(ramMb = 1_024L, cpuCores = 2))
    }

    @Test
    fun `medium ram and medium cpu cores returns MEDIUM tier`() {
        assertEquals(DeviceTier.MEDIUM, useCase(ramMb = 3_000L, cpuCores = 5))
    }

    @Test
    fun `high ram and high cpu cores returns HIGH tier`() {
        assertEquals(DeviceTier.HIGH, useCase(ramMb = 6_144L, cpuCores = 8))
    }

    // --- RAM boundary tests ---

    @Test
    fun `ram just below 2048 MB maps to LOW tier`() {
        assertEquals(DeviceTier.LOW, useCase(ramMb = 2_047L, cpuCores = 8))
    }

    @Test
    fun `ram at exactly 2048 MB maps to MEDIUM tier`() {
        assertEquals(DeviceTier.MEDIUM, useCase(ramMb = 2_048L, cpuCores = 5))
    }

    @Test
    fun `ram at exactly 4096 MB maps to MEDIUM tier`() {
        assertEquals(DeviceTier.MEDIUM, useCase(ramMb = 4_096L, cpuCores = 5))
    }

    @Test
    fun `ram just above 4096 MB maps to HIGH tier`() {
        assertEquals(DeviceTier.HIGH, useCase(ramMb = 4_097L, cpuCores = 8))
    }

    // --- CPU boundary tests ---

    @Test
    fun `cpu below 4 cores maps to LOW tier`() {
        assertEquals(DeviceTier.LOW, useCase(ramMb = 6_144L, cpuCores = 3))
    }

    @Test
    fun `cpu at exactly 4 cores maps to MEDIUM tier`() {
        assertEquals(DeviceTier.MEDIUM, useCase(ramMb = 6_144L, cpuCores = 4))
    }

    @Test
    fun `cpu at exactly 6 cores maps to MEDIUM tier`() {
        assertEquals(DeviceTier.MEDIUM, useCase(ramMb = 6_144L, cpuCores = 6))
    }

    @Test
    fun `cpu above 6 cores maps to HIGH tier`() {
        assertEquals(DeviceTier.HIGH, useCase(ramMb = 6_144L, cpuCores = 7))
    }

    // --- Conservative min-of logic ---

    @Test
    fun `high ram with low cpu cores returns LOW tier`() {
        assertEquals(DeviceTier.LOW, useCase(ramMb = 6_144L, cpuCores = 2))
    }

    @Test
    fun `low ram with high cpu cores returns LOW tier`() {
        assertEquals(DeviceTier.LOW, useCase(ramMb = 1_024L, cpuCores = 8))
    }

    @Test
    fun `medium ram with high cpu cores returns MEDIUM tier`() {
        assertEquals(DeviceTier.MEDIUM, useCase(ramMb = 3_000L, cpuCores = 8))
    }
}
