package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import javax.inject.Inject

/** Returns true when the other flavor's app is installed on the device. */
class CheckOtherAppInstalledUseCase @Inject constructor(
    private val repository: InterAppCommRepository,
) {
    operator fun invoke(currentPackage: String): Boolean =
        repository.isOtherAppInstalled(currentPackage)
}
