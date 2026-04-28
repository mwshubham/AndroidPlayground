plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dependency.guard)
}

android {
    namespace = "com.example.android.playground"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.android.playground"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Two flavors that install as separate apps so inter-app IPC demos work on the same device.
    // "default" keeps the original applicationId; "variant" appends ".variant".
    // Both flavors are signed with the same debug keystore, so signature-level permissions
    // are automatically granted between them in debug builds.
    flavorDimensions += "version"
    productFlavors {
        create("default") {
            dimension = "version"
            applicationId = "com.example.android.playground"
            versionNameSuffix = ""
        }
        create("variant") {
            dimension = "version"
            applicationId = "com.example.android.playground.variant"
            versionNameSuffix = "-variant"
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

// Guard release and debug compile/runtime classpaths.
// Run ./gradlew dependencyGuardBaseline to regenerate after intentional dep changes.
// Product flavors rename these to "default/variant" prefixed configurations.
dependencyGuard {
    configuration("defaultDebugCompileClasspath")
    configuration("defaultDebugRuntimeClasspath")
    configuration("defaultReleaseCompileClasspath")
    configuration("defaultReleaseRuntimeClasspath")
    configuration("variantDebugCompileClasspath")
    configuration("variantDebugRuntimeClasspath")
    configuration("variantReleaseCompileClasspath")
    configuration("variantReleaseRuntimeClasspath")
}

dependencies {
    // Feature modules
    implementation(project(":feature:feed:api"))
    implementation(project(":feature:feed:impl"))
    implementation(project(":feature:image-upload:api"))
    implementation(project(":feature:image-upload:impl"))
    implementation(project(":feature:login:api"))
    implementation(project(":feature:login:impl"))
    implementation(project(":feature:note:api"))
    implementation(project(":feature:note:impl"))
    implementation(project(":feature:media-orchestrator:api"))
    implementation(project(":feature:media-orchestrator:impl"))
    implementation(project(":feature:user-initiated-service:api"))
    implementation(project(":feature:user-initiated-service:impl"))
    implementation(project(":feature:crypto-security:api"))
    implementation(project(":feature:crypto-security:impl"))
    implementation(project(":feature:room-database:api"))
    implementation(project(":feature:room-database:impl"))
    implementation(project(":feature:inter-app-comm:api"))
    implementation(project(":feature:inter-app-comm:impl"))
    implementation(project(":feature:graphql:api"))
    implementation(project(":feature:graphql:impl"))

    // Core modules
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation
    implementation(libs.androidx.navigation3.compose)
    implementation(libs.androidx.navigation3.runtime)

    // Compose Libraries (specific to app, others moved to core:ui)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)

    // Hilt Libraries
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // WorkManager + Hilt-Work (for HiltWorkerFactory in Application)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Timber — structured logging; DebugTree is planted in the debug source set only
    implementation(libs.timber)

    // --- Debug tooling (only compiled into debug builds) ---

    // Chucker — in-app HTTP inspector
    // Wire ChuckerInterceptor + PlutoNetworkInterceptor into OkHttpClient when networking is added.
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    // Pluto — all-in-one in-app debugger overlay
    debugImplementation(libs.pluto)
    releaseImplementation(libs.pluto.no.op)
    debugImplementation(libs.pluto.network)
    releaseImplementation(libs.pluto.network.no.op)
    debugImplementation(libs.pluto.exceptions)
    releaseImplementation(libs.pluto.exceptions.no.op)

    // Flipper — desktop companion debug bridge (DEPRECATED by Meta, Nov 2024)
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network.plugin)
    debugImplementation(libs.soloader)

    // Stetho — Chrome DevTools bridge (unmaintained since ~2019)
    debugImplementation(libs.stetho)

    // LeakCanary — memory leak detection (self-initialising via ContentProvider, no init code needed)
    debugImplementation(libs.leakcanary)

    // ANR-WatchDog — detects and reports ANRs in debug builds
    debugImplementation(libs.anrwatchdog)

    // OkHttp Logging Interceptor — raw HTTP logs to Timber (wire into OkHttpClient when networking is added)
    debugImplementation(libs.okhttp.logging.interceptor)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
