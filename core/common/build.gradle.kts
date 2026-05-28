plugins {
    alias(libs.plugins.playground.android.library)
}

android {
    namespace = "com.example.android.playground.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
