plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.login"
}

dependencies {
    implementation(project(":feature:login:api"))
}
