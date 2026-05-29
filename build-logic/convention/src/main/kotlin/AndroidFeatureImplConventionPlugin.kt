import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("playground.android.library")
            pluginManager.apply("playground.android.compose")
            pluginManager.apply("playground.android.hilt")
            pluginManager.apply("app.cash.paparazzi")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // Core project modules every feature impl depends on
                "implementation"(project(":core:common"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:navigation"))

                // Android
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())

                // Testing
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("turbine").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(project(":core:testing"))
                "androidTestImplementation"(libs.findLibrary("androidx-junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx-espresso-core").get())
            }
        }
    }
}
