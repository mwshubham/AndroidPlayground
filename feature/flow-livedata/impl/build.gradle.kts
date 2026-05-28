plugins {
    alias(libs.plugins.playground.android.feature.impl)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.flowlivedata"
}

dependencies {
    implementation(project(":feature:flow-livedata:api"))
    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    testImplementation(libs.androidx.core.testing)
}
