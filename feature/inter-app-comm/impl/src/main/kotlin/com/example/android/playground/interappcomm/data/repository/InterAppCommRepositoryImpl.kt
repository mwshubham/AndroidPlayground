package com.example.android.playground.interappcomm.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.example.android.playground.interappcomm.domain.model.IpcChannel
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import com.example.android.playground.interappcomm.util.InterAppCommConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import javax.inject.Inject

class InterAppCommRepositoryImpl
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) : InterAppCommRepository {
        override fun isOtherAppInstalled(currentPackage: String): Boolean {
            val targetPackage = InterAppCommConstants.getTargetPackage(currentPackage)
            return try {
                context.packageManager.getPackageInfo(targetPackage, 0)
                true
            } catch (_: PackageManager.NameNotFoundException) {
                false
            }
        }

        /**
         * Reads the first signing certificate and returns its SHA-256 fingerprint.
         *
         * Uses [PackageManager.GET_SIGNING_CERTIFICATES] (API 28+), which is our minSdk,
         * so no version guard is needed.
         *
         * Format: "AA:BB:CC:…" — matching the output of:
         *   `apksigner verify --print-certs app.apk`
         */
        override fun getAppSignatureFingerprint(packageName: String): String? =
            runCatching {
                val info =
                    context.packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES,
                    )
                val cert = info.signingInfo?.apkContentsSigners?.firstOrNull() ?: return null
                val digest = MessageDigest.getInstance("SHA-256").digest(cert.toByteArray())
                digest.joinToString(":") { byte -> "%02X".format(byte) }
            }.getOrNull()

        override fun getIpcChannels(): List<IpcChannel> =
            listOf(
                IpcChannel(
                    method = IpcMethod.EXPLICIT_INTENT,
                    title = "Explicit Intent",
                    tagline = "Launch the other app's Activity and pass data via extras.",
                    syncAsync = "Async",
                    dataStyle = "Unstructured (Bundle extras)",
                    securityLabel = "<queries> + explicit package/class",
                    useCases =
                        listOf(
                            "Deep-link into a feature in a companion app",
                            "Pass a reference ID for the other app to look up",
                            "Custom tab / share-sheet integration",
                        ),
                ),
                IpcChannel(
                    method = IpcMethod.BROADCAST,
                    title = "Broadcast Receiver",
                    tagline = "Fire-and-forget event delivery to listening apps.",
                    syncAsync = "Async",
                    dataStyle = "Unstructured (Bundle extras)",
                    securityLabel = "receiverPermission + android:permission on receiver",
                    useCases =
                        listOf(
                            "Event fan-out: one sender, many receivers",
                            "State-change notifications (e.g. sync complete)",
                            "Wake the other app for lightweight triggers",
                        ),
                ),
                IpcChannel(
                    method = IpcMethod.CONTENT_PROVIDER,
                    title = "ContentProvider",
                    tagline = "Expose a structured data table with CRUD semantics.",
                    syncAsync = "Sync",
                    dataStyle = "Structured (Cursor / ContentValues)",
                    securityLabel = "readPermission + writePermission (separate gates)",
                    useCases =
                        listOf(
                            "Share a read-only dataset (e.g. contacts, media)",
                            "Allow a companion app to insert records into your DB",
                            "URI-grant delegation for one-off row access",
                        ),
                ),
                IpcChannel(
                    method = IpcMethod.MESSENGER,
                    title = "Messenger",
                    tagline = "Bidirectional message exchange via a Bound Service.",
                    syncAsync = "Async",
                    dataStyle = "Unstructured (Message + Bundle)",
                    securityLabel = "android:permission on service declaration",
                    useCases =
                        listOf(
                            "Simple request-response without defining a formal IDL",
                            "Streaming updates from a background service to a client",
                            "Single-threaded command queue (Handler processes sequentially)",
                        ),
                ),
                IpcChannel(
                    method = IpcMethod.AIDL,
                    title = "AIDL",
                    tagline = "Strongly-typed synchronous RPC across process boundaries.",
                    syncAsync = "Sync (or oneway for fire-and-forget)",
                    dataStyle = "Structured (typed interface via .aidl)",
                    securityLabel = "enforceCallingPermission() in every stub method",
                    useCases =
                        listOf(
                            "High-performance IPC needing multiple typed methods",
                            "Platform system services (e.g. audio, camera, media)",
                            "When you need synchronous replies with typed return values",
                        ),
                ),
            )
    }
