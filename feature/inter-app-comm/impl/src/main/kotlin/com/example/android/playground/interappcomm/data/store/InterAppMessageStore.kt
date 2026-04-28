package com.example.android.playground.interappcomm.data.store

import com.example.android.playground.interappcomm.domain.model.IpcMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Singleton in-memory store shared between:
 *  - [InterAppBroadcastReceiver]   → writes incoming broadcast messages
 *  - [InterAppContentProvider]     → reads/writes structured messages via Cursor
 *  - ViewModels via use-cases      → observe broadcast messages as a StateFlow
 *
 * Thread-safety:
 *  - [broadcastMessages] uses [MutableStateFlow.update] which is internally atomic.
 *  - [contentProviderMessages] uses a [ReentrantReadWriteLock] because ContentProvider
 *    methods may be called from multiple Binder threads simultaneously.
 */
@Singleton
class InterAppMessageStore @Inject constructor() {

    // ── Broadcast messages ────────────────────────────────────────────────────
    private val _broadcastMessages = MutableStateFlow<List<IpcMessage>>(emptyList())

    /** Observed by BroadcastViewModel to render the received-messages log. */
    val broadcastMessages: StateFlow<List<IpcMessage>> = _broadcastMessages.asStateFlow()

    fun addBroadcastMessage(message: IpcMessage) {
        _broadcastMessages.update { it + message }
    }

    fun clearBroadcastMessages() {
        _broadcastMessages.update { emptyList() }
    }

    // ── ContentProvider messages ──────────────────────────────────────────────
    private val cpLock = ReentrantReadWriteLock()
    private val cpMessages = mutableListOf<IpcMessage>()

    /** Returns a snapshot of the ContentProvider table. Thread-safe read. */
    fun getContentProviderMessages(): List<IpcMessage> =
        cpLock.read { cpMessages.toList() }

    /**
     * Inserts a row into the in-memory ContentProvider table.
     * Returns the new row's id string.
     */
    fun insertContentProviderMessage(message: IpcMessage): String {
        cpLock.write { cpMessages.add(message) }
        return message.id
    }

    /** Removes all rows from the ContentProvider table. */
    fun clearContentProviderMessages() {
        cpLock.write { cpMessages.clear() }
    }

    /** Removes a specific row by id. */
    fun deleteContentProviderMessage(id: String) {
        cpLock.write { cpMessages.removeAll { it.id == id } }
    }
}
