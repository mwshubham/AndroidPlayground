package com.example.android.playground.interappcomm.domain.repository

import com.example.android.playground.interappcomm.domain.model.IpcChannel

/**
 * Repository for utility queries needed by the home screen.
 * Each IPC channel's live I/O is handled directly by dedicated use-cases and
 * Android components (BroadcastReceiver, ContentProvider, Services).
 */
interface InterAppCommRepository {
    /**
     * Returns true when the other flavor's app is installed and visible
     * (requires `<queries>` block in the manifest for API 30+).
     */
    fun isOtherAppInstalled(currentPackage: String): Boolean

    /**
     * Computes the SHA-256 fingerprint of the first signing certificate for [packageName].
     * Returns null when the package is not installed or the cert cannot be read.
     * Format: "AA:BB:CC:…" (colon-separated uppercase hex bytes).
     */
    fun getAppSignatureFingerprint(packageName: String): String?

    /** Static metadata for each of the five IPC channels shown on the home screen. */
    fun getIpcChannels(): List<IpcChannel>
}
