plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.cryptosecurity"
}

dependencies {
    implementation(project(":feature:crypto-security:api"))
    implementation(libs.tink.android)
    implementation(libs.androidx.datastore.preferences)
}
