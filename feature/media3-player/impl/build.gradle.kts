plugins {
    alias(libs.plugins.playground.android.feature.impl)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.media3player"
}

dependencies {
    implementation(project(":feature:media3-player:api"))

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.common)
    implementation(libs.kotlinx.serialization.json)
}
