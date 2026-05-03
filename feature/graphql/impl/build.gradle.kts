plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.example.android.playground.graphql"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
    }
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(project(":feature:graphql:api"))

    // Core modules
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:common"))

    // Android Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose Libraries
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation3.compose)
    implementation(libs.androidx.navigation3.runtime)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // Networking — OkHttp for raw GraphQL POST requests
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Apollo Kotlin — type-safe GraphQL client (compare with raw OkHttp approach above)
    implementation(libs.apollo.runtime)

    // JSON serialisation
    implementation(libs.kotlinx.serialization.json)

    // DataStore — persists the GitHub PAT across app restarts
    implementation(libs.androidx.datastore.preferences)

    // Testing
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Apollo Kotlin code generation configuration.
// The plugin reads .graphql files at build time and generates type-safe Kotlin classes.
// Generated output: build/generated/source/apollo/
apollo {
    service("github") {
        // Package for all generated classes (e.g. SearchReposQuery, SearchReposQuery.Data ...)
        packageName.set("com.example.android.playground.graphql.apollo")

        // Custom scalar mappings: tell Apollo how to represent GitHub's non-standard scalars.
        // Without these, Apollo would generate 'Any' for unknown scalars.
        mapScalarToKotlinString("URI") // GitHub URLs  → kotlin.String
        mapScalarToKotlinString("DateTime") // ISO-8601 dates → kotlin.String
    }
}
