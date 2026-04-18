package com.example.android.playground

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AndroidPlaygroundApplication :
    Application(),
    Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    // WorkManager reads this configuration on first use because auto-initialization is
    // disabled in AndroidManifest.xml (WorkManagerInitializer removed from startup).
    // HiltWorkerFactory enables @HiltWorker dependency injection in CoroutineWorkers.
    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build()
}
