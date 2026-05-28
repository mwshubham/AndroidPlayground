plugins {
    alias(libs.plugins.playground.android.library)
    alias(libs.plugins.playground.android.hilt)
}

android {
    namespace = "com.example.android.playground.analytics"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.timber)
}
