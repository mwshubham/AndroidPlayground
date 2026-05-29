import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    // compileOnly for type-safe DSL access in convention plugin code
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.paparazzi.gradlePlugin)
    // implementation so the JAR is on the runtime classpath and can be applied to subprojects
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "playground.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "playground.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "playground.android.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("androidFeatureApi") {
            id = "playground.android.feature.api"
            implementationClass = "AndroidFeatureApiConventionPlugin"
        }
        register("androidFeatureImpl") {
            id = "playground.android.feature.impl"
            implementationClass = "AndroidFeatureImplConventionPlugin"
        }
        register("androidScreenshotTest") {
            id = "playground.android.screenshotTest"
            implementationClass = "ScreenshotTestConventionPlugin"
        }
    }
}
