plugins {
    id("playground.android.library")
}

android {
    namespace = "com.example.android.playground.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
