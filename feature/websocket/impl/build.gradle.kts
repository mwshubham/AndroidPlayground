plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.websocket"
}

dependencies {
    implementation(project(":feature:websocket:api"))
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
