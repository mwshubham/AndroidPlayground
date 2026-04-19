package com.example.android.playground

import android.app.Application

/**
 * No-op release counterpart of the debug [DebugToolsInitializer].
 * All debug tooling dependencies are excluded from the release build.
 */
object DebugToolsInitializer {
    fun init(app: Application) = Unit
}
