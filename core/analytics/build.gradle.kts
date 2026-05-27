plugins {
    id("playground.android.library")
    id("playground.android.hilt")
}

android {
    namespace = "com.example.android.playground.analytics"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.timber)
}
