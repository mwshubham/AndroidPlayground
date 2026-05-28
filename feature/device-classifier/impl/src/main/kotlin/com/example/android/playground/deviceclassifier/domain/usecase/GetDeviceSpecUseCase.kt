package com.example.android.playground.deviceclassifier.domain.usecase

import com.example.android.playground.deviceclassifier.domain.model.DeviceSpec
import com.example.android.playground.deviceclassifier.domain.repository.DeviceClassifierRepository
import javax.inject.Inject

class GetDeviceSpecUseCase
    @Inject
    constructor(
        private val repository: DeviceClassifierRepository,
    ) {
        suspend operator fun invoke(): DeviceSpec = repository.getDeviceSpec()
    }
