package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import javax.inject.Inject

/** Returns static metadata for all five IPC channels for the home screen grid. */
class GetIpcChannelsUseCase @Inject constructor(
    private val repository: InterAppCommRepository,
) {
    operator fun invoke(): List<IpcChannel> = repository.getIpcChannels()
}
