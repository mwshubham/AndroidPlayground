plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.roomdatabase"
}

dependencies {
    implementation(project(":feature:room-database:api"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
