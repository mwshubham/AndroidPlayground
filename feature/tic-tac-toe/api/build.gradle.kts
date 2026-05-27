plugins {
    id("playground.android.feature.api")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.tictactoe.api"
}
