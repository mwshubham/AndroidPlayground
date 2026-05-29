plugins {
    alias(libs.plugins.playground.android.feature.impl)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.sse"
}

dependencies {
    implementation(project(":feature:sse:api"))
    implementation(libs.okhttp)
    implementation(libs.okhttp.sse)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
