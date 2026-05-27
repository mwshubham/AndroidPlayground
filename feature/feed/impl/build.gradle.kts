plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.feed.impl"
}

dependencies {
    implementation(project(":feature:feed:api"))
    implementation(project(":feature:note:api"))
    implementation(project(":feature:login:api"))
    implementation(project(":feature:image-upload:api"))
    implementation(project(":feature:media-orchestrator:api"))
    implementation(project(":feature:user-initiated-service:api"))
    implementation(project(":feature:crypto-security:api"))
    implementation(project(":feature:room-database:api"))
    implementation(project(":feature:inter-app-comm:api"))
    implementation(project(":feature:graphql:api"))
    implementation(project(":feature:media3-player:api"))
    implementation(project(":feature:websocket:api"))
    implementation(project(":feature:sse:api"))
    implementation(project(":feature:grpc:api"))
    implementation(project(":feature:tic-tac-toe:api"))
    implementation(project(":feature:flow-livedata:api"))
}
