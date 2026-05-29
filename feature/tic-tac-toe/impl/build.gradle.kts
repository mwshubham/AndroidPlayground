plugins {
    alias(libs.plugins.playground.android.feature.impl)
    alias(libs.plugins.playground.android.screenshot.test)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.tictactoe"
}

dependencies {
    implementation(project(":feature:tic-tac-toe:api"))
    implementation(libs.timber)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
