package com.example.android.playground.interappcomm.util

/**
 * Central constants for the Inter-App Communication feature.
 *
 * Using a top-level object keeps the package name strings in a single place.
 * All package/class names that appear in both the "default" and "variant" flavors
 * are relative to the base package — the actual applicationId is detected at
 * runtime via [android.content.Context.getPackageName].
 */
object InterAppCommConstants {

    /** applicationId of the "default" flavor. */
    const val DEFAULT_APP_PACKAGE = "com.example.android.playground"

    /** applicationId of the "variant" flavor. */
    const val VARIANT_APP_PACKAGE = "com.example.android.playground.variant"

    /**
     * Given the current app's package, returns the OTHER app's package.
     * Works correctly regardless of which flavor is currently running.
     */
    fun getTargetPackage(currentPackage: String): String =
        if (currentPackage == DEFAULT_APP_PACKAGE) VARIANT_APP_PACKAGE else DEFAULT_APP_PACKAGE

    // ── Signature-level custom permission ───────────────────────────────────
    /**
     * Declared as `protectionLevel="signature"` in the app manifest.
     * Automatically granted on install when both apps share the same signing cert
     * (debug keystore for both flavors → always granted in dev builds).
     * Any unsigned or differently-signed app will be silently refused.
     */
    const val CUSTOM_PERMISSION = "com.example.android.playground.permission.INTER_APP_COMM"

    // ── Broadcast ────────────────────────────────────────────────────────────
    /** Custom broadcast action — avoids matching unrelated system broadcasts. */
    const val BROADCAST_ACTION = "com.example.android.playground.action.INTER_APP_MESSAGE"

    /** Bundle key: the human-readable message text. */
    const val KEY_MESSAGE_CONTENT = "key_message_content"

    /** Bundle key: package name of the sending app. */
    const val KEY_SENDER_PACKAGE = "key_sender_package"

    /** Bundle key: ISO-8601 timestamp string. */
    const val KEY_TIMESTAMP = "key_timestamp"

    // ── ContentProvider ──────────────────────────────────────────────────────
    /** Column name for the row ID in the ContentProvider. */
    const val COLUMN_ID = "id"

    /** Column name for the message text. */
    const val COLUMN_CONTENT = "content"

    /** Column name for the sender package name. */
    const val COLUMN_SENDER = "sender"

    /** Column name for the Unix-epoch timestamp (ms). */
    const val COLUMN_TIMESTAMP = "timestamp"

    /** Provider path segment for the messages table. */
    const val PATH_MESSAGES = "messages"

    // ── Messenger ────────────────────────────────────────────────────────────
    /** Fully-qualified class name for the Messenger service (same in both flavors). */
    const val MESSENGER_SERVICE_CLASS =
        "com.example.android.playground.interappcomm.data.service.InterAppMessengerService"

    /** Message.what value: client sends a text message. */
    const val MSG_SEND_TEXT = 1

    /** Message.what value: service echoes the message back to the client. */
    const val MSG_ECHO = 2

    // ── AIDL ─────────────────────────────────────────────────────────────────
    /** Fully-qualified class name for the AIDL service (same in both flavors). */
    const val AIDL_SERVICE_CLASS =
        "com.example.android.playground.interappcomm.data.service.InterAppAidlService"
}
