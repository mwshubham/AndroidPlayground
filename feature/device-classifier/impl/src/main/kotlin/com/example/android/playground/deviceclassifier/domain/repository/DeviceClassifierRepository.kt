package com.example.android.playground.deviceclassifier.domain.repository

import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec

interface DeviceClassifierRepository {
    suspend fun getDeviceSpec(): DeviceSpec
}
