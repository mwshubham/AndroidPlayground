package com.example.android.playground

import android.app.Application
import android.os.StrictMode
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import android.content.Context
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin.SharedPreferencesDescriptor
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.github.anrwatchdog.ANRWatchDog
import com.pluto.Pluto
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.network.PlutoNetworkPlugin
import timber.log.Timber

/**
 * Initialises all debug-only tooling. Called from [AndroidPlaygroundApplication.onCreate].
 * This file exists only in src/debug — the release counterpart is a no-op.
 *
 * Tool notes:
 *  • Flipper   — deprecated by Meta (Nov 2024); requires the Flipper desktop companion app.
 *               Download: https://fbflipper.com/
 *  • Stetho    — unmaintained since ~2019; inspect via chrome://inspect in Chrome DevTools.
 *  • LeakCanary — self-initialises via its ContentProvider; no manual code required.
 */
object DebugToolsInitializer {

    fun init(app: Application) {
        initTimber()
        initStrictMode()
        initFlipper(app)
        initStetho(app)
        initPluto(app)
        initAnrWatchDog()
        // LeakCanary initialises automatically — no call needed here.
    }

    // ── Timber ──────────────────────────────────────────────────────────────────────────────────

    private fun initTimber() {
        // DebugTree logs with the calling class name and line number as the tag.
        // Logs from PlutoExceptionsPlugin are also piped through Timber automatically.
        Timber.plant(Timber.DebugTree())
    }

    // ── StrictMode ──────────────────────────────────────────────────────────────────────────────

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll() // disk reads/writes, network, slow calls, unbuffered IO, resource mismatches
                .penaltyLog()
                .build(),
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll() // leaked cursors, closeables, SQLite objects, activity/fragment leaks
                .penaltyLog()
                .build(),
        )
    }

    // ── Flipper (deprecated by Meta, Nov 2024) ───────────────────────────────────────────────────

    private fun initFlipper(app: Application) {
        SoLoader.init(app, false)
        if (FlipperUtils.shouldEnableFlipper(app)) {
            AndroidFlipperClient.getInstance(app).apply {
                addPlugin(NetworkFlipperPlugin())
                addPlugin(DatabasesFlipperPlugin(app))
                addPlugin(
                    SharedPreferencesFlipperPlugin(
                        app,
                        listOf(
                            // Default app prefs
                            SharedPreferencesDescriptor("${app.packageName}_preferences", Context.MODE_PRIVATE),
                            // Tink AndroidKeysetManager stores the wrapped keyset here
                            SharedPreferencesDescriptor("tink_aead_prefs", Context.MODE_PRIVATE),
                        ),
                    ),
                )
                start()
            }
        }
    }

    // ── Stetho (unmaintained since ~2019) ────────────────────────────────────────────────────────

    private fun initStetho(app: Application) {
        Stetho.initializeWithDefaults(app)
    }

    // ── Pluto ────────────────────────────────────────────────────────────────────────────────────

    private fun initPluto(app: Application) {
        Pluto.Installer(app)
            .addPlugin(PlutoNetworkPlugin("network"))
            // Room DB and DataStore inspection is available natively in Android Studio's
            // App Inspection panel (Database Inspector / DataStore tab) — no Pluto plugin needed.
            .addPlugin(PlutoExceptionsPlugin())
            .install()
    }

    // ── ANR-WatchDog ─────────────────────────────────────────────────────────────────────────────

    private fun initAnrWatchDog() {
        ANRWatchDog().start()
    }

    // ── Networking interceptors (TODO) ───────────────────────────────────────────────────────────

    /**
     * When OkHttp is added, wire these interceptors into your OkHttpClient:
     *
     * ```kotlin
     * OkHttpClient.Builder()
     *     // In-app HTTP log viewer — tap the Chucker notification to browse requests
     *     .addInterceptor(ChuckerInterceptor.Builder(context).build())
     *     // Raw HTTP log to Logcat via Timber
     *     .addInterceptor(
     *         HttpLoggingInterceptor { Timber.tag("OkHttp").d(it) }.apply {
     *             level = HttpLoggingInterceptor.Level.BODY
     *         }
     *     )
     *     // Pluto network monitoring overlay
     *     .addInterceptor(NetworkInterceptor())
     *     .build()
     * ```
     *
     * Imports needed:
     *   com.chuckerteam.chucker.api.ChuckerInterceptor
     *   okhttp3.logging.HttpLoggingInterceptor
     *   com.pluto.plugins.network.intercept.NetworkInterceptor
     */
}
