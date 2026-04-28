package com.example.android.playground.interappcomm.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Receives broadcasts sent by the OTHER app via [SendBroadcastUseCase].
 *
 * Security layers:
 *  1. Manifest `android:permission` — the OS checks that the SENDER holds
 *     [CUSTOM_PERMISSION] before delivering to this receiver. Unsigned apps
 *     calling sendBroadcast() with our action simply get no delivery.
 *  2. `android:exported="true"` is required to receive from external packages,
 *     but the permission gate prevents abuse.
 *  3. Inside [onReceive] we additionally verify the sender's package as a
 *     defence-in-depth measure — illustrates how to inspect calling identity.
 *
 * @AndroidEntryPoint enables Hilt field injection into this BroadcastReceiver.
 * The injected [store] is the Singleton [InterAppMessageStore], so the update is
 * immediately visible to any ViewModel observing [InterAppMessageStore.broadcastMessages].
 */
@AndroidEntryPoint
class InterAppBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var store: InterAppMessageStore

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != InterAppCommConstants.BROADCAST_ACTION) return

        val content = intent.getStringExtra(InterAppCommConstants.KEY_MESSAGE_CONTENT)
            ?: return
        val senderPackage = intent.getStringExtra(InterAppCommConstants.KEY_SENDER_PACKAGE)
            ?: "unknown"
        val timestamp = intent.getLongExtra(
            InterAppCommConstants.KEY_TIMESTAMP,
            System.currentTimeMillis(),
        )

        // Defence-in-depth: verify sender is the expected companion app.
        // The manifest permission already guarantees this, but explicit checks
        // show developers how to perform identity verification in code too.
        val expectedSender = InterAppCommConstants.getTargetPackage(context.packageName)
        if (senderPackage != expectedSender) {
            // Log the anomaly but do not crash — the manifest permission should have
            // prevented this, so reaching here is unexpected.
            android.util.Log.w(
                "InterAppBroadcastReceiver",
                "Received broadcast from unexpected sender: $senderPackage (expected $expectedSender)",
            )
        }

        val message = IpcMessage(
            content = content,
            sender = senderPackage,
            timestamp = timestamp,
            method = IpcMethod.BROADCAST,
            direction = MessageDirection.RECEIVED,
        )
        store.addBroadcastMessage(message)
    }
}
