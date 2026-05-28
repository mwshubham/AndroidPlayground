plugins {
    alias(libs.plugins.playground.android.library)
}

android {
    namespace = "com.example.android.playground.core.testing"
}

dependencies {
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
}
