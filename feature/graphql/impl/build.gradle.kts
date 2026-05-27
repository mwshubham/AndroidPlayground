
plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.example.android.playground.graphql"
}

dependencies {
    implementation(project(":feature:graphql:api"))

    // Networking
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Apollo Kotlin — type-safe GraphQL client
    implementation(libs.apollo.runtime)

    // JSON serialisation
    implementation(libs.kotlinx.serialization.json)

    // DataStore — persists the GitHub PAT across app restarts
    implementation(libs.androidx.datastore.preferences)
}

// Apollo Kotlin code generation configuration.
apollo {
    service("github") {
        packageName.set("com.example.android.playground.graphql.apollo")
        mapScalarToKotlinString("URI")
        mapScalarToKotlinString("DateTime")
    }
}
