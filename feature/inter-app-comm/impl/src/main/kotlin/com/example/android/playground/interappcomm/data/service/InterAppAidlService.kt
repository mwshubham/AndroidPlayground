package com.example.android.playground.interappcomm.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.android.playground.interappcomm.IInterAppService
import com.example.android.playground.interappcomm.data.store.InterAppMessageStore
import com.example.android.playground.interappcomm.domain.model.IpcMessage
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.model.MessageDirection
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * AIDL Bound Service for the inter-app AIDL IPC demo.
 *
 * Implements the [IInterAppService] stub generated from [IInterAppService.aidl].
 *
 * Security layers:
 *  1. Manifest `android:permission` — OS-level gate before [onBind] is reached.
 *  2. [enforceCallingPermission] inside EACH stub method — defence-in-depth for
 *     the case where a permission-hold check is bypassed by a rooted device or
 *     a compromised system process. Throws [SecurityException] immediately.
 *  3. [Binder.getCallingUid] / [Binder.getCallingPid] — logged for auditing.
 *  4. [linkToDeath] in [onBind] — lets the server clean up when the client process dies.
 *
 * Threading: AIDL stub methods run on the Binder thread pool (NOT the main thread).
 * All mutable state (the messages list) is therefore protected by a [ReentrantReadWriteLock].
 */
@AndroidEntryPoint
class InterAppAidlService : Service() {
    @Inject
    lateinit var store: InterAppMessageStore

    // Local per-service message list (separate from the shared store, for demo clarity)
    private val messagesLock = ReentrantReadWriteLock()
    private val aidlMessages = mutableListOf<String>()

    private val binder =
        object : IInterAppService.Stub() {
            /**
             * Synchronous ping — blocks caller until we return.
             * enforceCallingPermission: throws SecurityException if caller lost the permission
             * between bind-time and this call (e.g. permission revoked via root).
             */
            override fun ping(message: String): String {
                enforcePermission()
                logCaller("ping")
                return "Pong from ${applicationContext.packageName}: $message"
            }

            /** Returns a snapshot of all messages stored on this service side. */
            override fun getMessages(): List<String> {
                enforcePermission()
                logCaller("getMessages")
                return messagesLock.read { aidlMessages.toList() }
            }

            /**
             * Fire-and-forget message post — declared 'oneway' in the AIDL so the
             * caller does NOT block waiting for us to process it.
             * Note: cannot return a value or throw checked exceptions from 'oneway' methods.
             */
            override fun postMessage(
                content: String,
                senderPackage: String,
            ) {
                // No enforcePermission() for oneway — caller already passed permission at bind time.
                // The 'oneway' keyword runs this on the server's Binder thread without a reply.
                logCaller("postMessage")
                val storedContent = "[$senderPackage]: $content"
                messagesLock.write { aidlMessages.add(storedContent) }
                store.addBroadcastMessage(
                    IpcMessage(
                        content = content,
                        sender = senderPackage,
                        method = IpcMethod.AIDL,
                        direction = MessageDirection.RECEIVED,
                    ),
                )
            }

            private fun enforcePermission() {
                // enforceCallingPermission throws SecurityException and logs if the calling
                // process does not hold CUSTOM_PERMISSION. This is the recommended AIDL pattern.
                enforceCallingPermission(
                    InterAppCommConstants.CUSTOM_PERMISSION,
                    "Caller must hold ${InterAppCommConstants.CUSTOM_PERMISSION}",
                )
            }

            private fun logCaller(method: String) {
                val uid = Binder.getCallingUid()
                val pid = Binder.getCallingPid()
                Timber.d("$method: callerUid=$uid callerPid=$pid")
            }
        }

    override fun onBind(intent: Intent): IBinder {
        Timber.d("Client bound")

        // linkToDeath: register a DeathRecipient so the service is notified when
        // the client process dies unexpectedly — allows resource cleanup.
        try {
            binder.asBinder().linkToDeath(
                {
                    Timber.d("Client died")
                    // In production: clean up any per-client state here
                },
                0,
            )
        } catch (e: Exception) {
            // linkToDeath may throw if the binder is already dead — log and continue
            Timber.d(e, "linkToDeath failed; binder may already be dead")
        }
        return binder
    }
}
