package com.example.android.playground.deviceclassifier.data.repository

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import com.example.android.playground.deviceclassifier.domain.repository.DeviceClassifierRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceClassifierRepositoryImpl
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) : DeviceClassifierRepository {
        override suspend fun getDeviceSpec(): DeviceSpec {
            val activityManager = context.getSystemService(ActivityManager::class.java)
            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            val ramMb = memoryInfo.totalMem / BYTES_PER_MB
            val cpuCores = Runtime.getRuntime().availableProcessors()
            val apiLevel = Build.VERSION.SDK_INT
            return DeviceSpec(ramMb = ramMb, cpuCores = cpuCores, apiLevel = apiLevel)
        }

        private companion object {
            const val BYTES_PER_MB = 1_048_576L
        }
    }
