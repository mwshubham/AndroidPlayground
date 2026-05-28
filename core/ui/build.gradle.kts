plugins {
    alias(libs.plugins.playground.android.library)
    alias(libs.plugins.playground.android.compose)
    alias(libs.plugins.playground.android.hilt)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.android.playground.core.ui"
}

dependencies {
    implementation(project(":core:analytics"))

    implementation(libs.androidx.core.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
