package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Returns the StateFlow of messages that have arrived via the BroadcastReceiver.
 * The BroadcastReceiver writes into [InterAppMessageStore]; ViewModels observe this use-case.
 */
class GetBroadcastMessagesUseCase @Inject constructor(
    private val store: com.example.android.playground.interappcomm.data.store.InterAppMessageStore,
) {
    operator fun invoke(): StateFlow<List<IpcMessage>> = store.broadcastMessages
}
