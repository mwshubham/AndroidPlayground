plugins {
    id("playground.android.feature.impl")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.android.playground.annotationprocessing"
}

dependencies {
    implementation(project(":feature:annotation-processing:api"))
    // Annotation class on compile classpath so @AutoToString can be used in source
    implementation(project(":feature:annotation-processing:processor"))
    // KSP wires the processor to run at compile time
    ksp(project(":feature:annotation-processing:processor"))
    implementation(libs.timber)
}
