plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.deviceclassifier"
}

dependencies {
    implementation(project(":feature:device-classifier:api"))
    implementation(libs.timber)
}
