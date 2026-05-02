package com.example.android.playground.interappcomm.domain.model

/**
 * Identifies WHICH of the five IPC mechanisms was used to deliver a message.
 * Drives the icon/label shown next to each item in [MessageLogList].
 */
enum class IpcMethod(
    val label: String,
) {
    EXPLICIT_INTENT("Explicit Intent"),
    BROADCAST("Broadcast"),
    CONTENT_PROVIDER("ContentProvider"),
    MESSENGER("Messenger"),
    AIDL("AIDL"),
}
