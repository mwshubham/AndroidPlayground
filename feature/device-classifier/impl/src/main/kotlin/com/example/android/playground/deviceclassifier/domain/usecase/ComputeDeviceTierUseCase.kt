package com.example.android.playground.deviceclassifier.domain.usecase

import com.example.android.playground.deviceclassifier.domain.model.DeviceTier
import javax.inject.Inject

/**
 * Computes a [DeviceTier] from raw RAM and CPU values.
 *
 * Thresholds:
 *   RAM  < 2 048 MB  → LOW  |  2 048–4 096 MB → MEDIUM  |  > 4 096 MB → HIGH
 *   CPU  < 4 cores   → LOW  |  4–6 cores      → MEDIUM  |  > 6 cores  → HIGH
 *
 * Final tier = min(ram_tier, cpu_tier) — conservative: a device with low RAM is
 * never promoted to HIGH just because it has many CPU cores.
 */
class ComputeDeviceTierUseCase
    @Inject
    constructor() {
        operator fun invoke(
            ramMb: Long,
            cpuCores: Int,
        ): DeviceTier {
            val ramTier =
                when {
                    ramMb < RAM_LOW_THRESHOLD_MB -> DeviceTier.LOW
                    ramMb <= RAM_MEDIUM_THRESHOLD_MB -> DeviceTier.MEDIUM
                    else -> DeviceTier.HIGH
                }
            val cpuTier =
                when {
                    cpuCores < CPU_LOW_THRESHOLD -> DeviceTier.LOW
                    cpuCores <= CPU_MEDIUM_THRESHOLD -> DeviceTier.MEDIUM
                    else -> DeviceTier.HIGH
                }
            return minOf(ramTier, cpuTier)
        }

        private companion object {
            const val RAM_LOW_THRESHOLD_MB = 2_048L
            const val RAM_MEDIUM_THRESHOLD_MB = 4_096L
            const val CPU_LOW_THRESHOLD = 4
            const val CPU_MEDIUM_THRESHOLD = 6
        }
    }
