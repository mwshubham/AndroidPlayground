package com.example.android.playground.common

/**
 * Shared timing values used across background work and simulated operations.
 */
object TimingConstants {
    /**
     * Simulated chunk delay used by transfer/upload background work to produce visible progress.
     */
    const val FILE_TRANSFER_CHUNK_DELAY_MS = 2_000L

    /**
     * Lightweight simulated delay for post-upload association work.
     */
    const val BATCH_ASSOCIATION_DELAY_MS = 300L

    /**
     * Emit chunk-level progress logs for the first chunk, last chunk, and every Nth chunk.
     */
    const val PROGRESS_LOG_SAMPLE_INTERVAL = 3
}
