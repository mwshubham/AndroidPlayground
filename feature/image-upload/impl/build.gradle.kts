plugins {
    alias(libs.plugins.playground.android.feature.impl)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.imageupload"
}

dependencies {
    implementation(project(":feature:image-upload:api"))
}
