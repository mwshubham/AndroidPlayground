package com.example.android.playground.interappcomm.domain.model

import java.util.UUID

/**
 * Represents a single IPC message regardless of the delivery mechanism.
 *
 * @param id         Random UUID for list keying; not the ContentProvider row id.
 * @param content    Human-readable payload that was exchanged.
 * @param sender     Package name of the originating app.
 * @param timestamp  Unix epoch (ms) when the message was created or received.
 * @param method     Which of the five IPC channels carried this message.
 * @param direction  SENT by this app or RECEIVED from the other app.
 */
data class IpcMessage(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val sender: String,
    val timestamp: Long = System.currentTimeMillis(),
    val method: IpcMethod,
    val direction: MessageDirection,
)
