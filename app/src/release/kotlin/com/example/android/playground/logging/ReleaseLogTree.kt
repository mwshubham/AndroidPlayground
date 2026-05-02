package com.example.android.playground.logging

import android.util.Log
import timber.log.Timber

/**
 * Production-safe Timber tree that only forwards actionable warning and error logs.
 */
internal class ReleaseLogTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        val resolvedTag = tag ?: DEFAULT_TAG
        when (priority) {
            Log.WARN -> Log.w(resolvedTag, message, t)
            Log.ERROR, Log.ASSERT -> Log.e(resolvedTag, message, t)
            else -> Unit
        }
    }

    private companion object {
        private const val DEFAULT_TAG = "AndroidPlayground"
    }
}
