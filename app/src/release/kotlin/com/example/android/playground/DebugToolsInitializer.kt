package com.example.android.playground

import android.app.Application
import com.example.android.playground.logging.ReleaseLogTree
import timber.log.Timber

/**
 * Release counterpart of the debug [DebugToolsInitializer].
 * Only warning and error logs are emitted in release builds.
 */
object DebugToolsInitializer {
    fun init(app: Application) {
        if (Timber.forest().isEmpty()) {
            Timber.plant(ReleaseLogTree())
        }
    }
}
