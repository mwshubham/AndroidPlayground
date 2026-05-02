package com.example.android.playground.interappcomm.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Bound Service for the Messenger IPC demo.
 *
 * Architecture:
 *  - Server exposes a [Messenger] wrapping an [IncomingHandler].
 *  - Client sends [Message] objects via that Messenger.
 *  - For replies, the client puts its own [Messenger] in [Message.replyTo].
 *
 * Security layers:
 *  1. Manifest `android:permission` — the OS rejects any bindService() call
 *     whose caller does not hold [CUSTOM_PERMISSION]. Our [onBind] is never
 *     reached by unauthorized apps.
 *  2. [onBind] checks [Binder.getCallingUid] — defence-in-depth; logs the
 *     connecting app's UID and package for auditing.
 *  3. [IncomingHandler] validates message data — never assume the Bundle keys
 *     are well-formed; malformed bundles are silently ignored.
 *
 * Threading: [IncomingHandler] runs on the MAIN thread (Looper.getMainLooper).
 * For CPU-heavy work inside a handler, post to a worker coroutine or use
 * [Handler(workerLooper)] instead.
 */
@AndroidEntryPoint
class InterAppMessengerService : Service() {
    @Inject
    lateinit var store: InterAppMessageStore

    private lateinit var incomingMessenger: Messenger

    inner class IncomingHandler(
        looper: Looper,
    ) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                InterAppCommConstants.MSG_SEND_TEXT -> {
                    val data: Bundle = msg.data ?: return
                    val content = data.getString(InterAppCommConstants.KEY_MESSAGE_CONTENT) ?: return
                    val senderPackage = data.getString(InterAppCommConstants.KEY_SENDER_PACKAGE) ?: "unknown"

                    // Store the received message
                    store.addBroadcastMessage(
                        IpcMessage(
                            content = content,
                            sender = senderPackage,
                            method = IpcMethod.MESSENGER,
                            direction = MessageDirection.RECEIVED,
                        ),
                    )

                    // Send echo reply using msg.replyTo if the client provided one
                    val replyTo: Messenger? = msg.replyTo
                    if (replyTo != null) {
                        val reply = Message.obtain(null, InterAppCommConstants.MSG_ECHO)
                        reply.data =
                            Bundle().apply {
                                putString(
                                    InterAppCommConstants.KEY_MESSAGE_CONTENT,
                                    "Echo from ${applicationContext.packageName}: $content",
                                )
                                putString(InterAppCommConstants.KEY_SENDER_PACKAGE, applicationContext.packageName)
                            }
                        try {
                            replyTo.send(reply)
                        } catch (e: RemoteException) {
                            // Client died before the echo arrived — the reply is intentionally discarded
                            Timber.d(e, "Client died before reply could be sent")
                        }
                    }
                }

                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        incomingMessenger = Messenger(IncomingHandler(Looper.getMainLooper()))
    }

    /**
     * Called by the OS AFTER the permission check from the manifest.
     * We log the caller's UID for auditing.
     */
    override fun onBind(intent: Intent): IBinder {
        val callerUid = Binder.getCallingUid()
        val callerPackage = packageManager.getNameForUid(callerUid) ?: "unknown"
        Timber.d("Client bound: package=$callerPackage uid=$callerUid")
        return incomingMessenger.binder
    }
}
