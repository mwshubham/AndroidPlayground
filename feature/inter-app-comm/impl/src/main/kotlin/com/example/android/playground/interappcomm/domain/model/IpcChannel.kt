package com.example.android.playground.interappcomm.domain.model

/**
 * Metadata describing one IPC channel — shown in [IpcMethodCard] on the home screen.
 *
 * @param method          Which mechanism this describes.
 * @param title           Short display name (e.g. "Explicit Intent").
 * @param tagline         One-sentence problem statement.
 * @param syncAsync       "Sync" or "Async" — whether the caller blocks for a reply.
 * @param dataStyle       "Structured" or "Unstructured" — typed interface vs raw Bundle/bytes.
 * @param securityLabel   Human-readable description of the security gate used.
 * @param useCases        2-3 bullet-point strings showing ideal use-cases.
 */
data class IpcChannel(
    val method: IpcMethod,
    val title: String,
    val tagline: String,
    val syncAsync: String,
    val dataStyle: String,
    val securityLabel: String,
    val useCases: List<String>,
)
