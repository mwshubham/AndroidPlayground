package com.example.android.playground.interappcomm.domain.usecase

import android.content.Context
import android.content.Intent
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Sends a broadcast to the other app's [InterAppBroadcastReceiver].
 *
 * Security note: the second argument to [Context.sendBroadcast] is the
 * *receiver permission* — the OS will only deliver the broadcast to receivers
 * whose declaring app holds [CUSTOM_PERMISSION]. This prevents a third-party app
 * from registering a receiver with the same action and intercepting our messages.
 */
class SendBroadcastUseCase
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) {
        operator fun invoke(content: String): IpcMessage {
            val intent =
                Intent(InterAppCommConstants.BROADCAST_ACTION).apply {
                    putExtra(InterAppCommConstants.KEY_MESSAGE_CONTENT, content)
                    putExtra(InterAppCommConstants.KEY_SENDER_PACKAGE, context.packageName)
                    putExtra(InterAppCommConstants.KEY_TIMESTAMP, System.currentTimeMillis())
                }
            // Pass receiverPermission so only apps holding INTER_APP_COMM receive this broadcast.
            context.sendBroadcast(intent, InterAppCommConstants.CUSTOM_PERMISSION)

            return IpcMessage(
                content = content,
                sender = context.packageName,
                method = IpcMethod.BROADCAST,
                direction = MessageDirection.SENT,
            )
        }
    }
