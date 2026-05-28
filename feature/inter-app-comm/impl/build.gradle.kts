plugins {
    alias(libs.plugins.playground.android.feature.impl)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.interappcomm"
    buildFeatures {
        // Required to compile .aidl interface files for the AIDL IPC demo
        aidl = true
    }
}

dependencies {
    implementation(project(":feature:inter-app-comm:api"))
    implementation(libs.timber)

    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("app.cash.turbine:turbine:1.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}
