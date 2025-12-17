
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

// Apply Detekt and Ktlint to all subprojects
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<DetektExtension> {
        config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        autoCorrect = false
        parallel = true
    }

    // Configure Ktlint using afterEvaluate to ensure plugin is applied
    afterEvaluate {
        extensions.findByName("ktlint")?.run {
            this as KtlintExtension
            android.set(true)
            ignoreFailures.set(false)
            reporters {
                reporter(ReporterType.PLAIN)
                reporter(ReporterType.CHECKSTYLE)
            }
            filter {
                exclude("**/generated/**")
                exclude("**/build/**")
            }
        }
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
}

// Root level tasks for convenience
tasks.register("ktlintFormatAll") {
    group = "formatting"
    description = "Format all Kotlin code using ktlint"
    dependsOn(subprojects.map { "${it.path}:ktlintFormat" })
}

tasks.register("ktlintCheckAll") {
    group = "verification"
    description = "Check all Kotlin code formatting using ktlint"
    dependsOn(subprojects.map { "${it.path}:ktlintCheck" })
}

tasks.register("detektCheckAll") {
    group = "verification"
    description = "Run detekt static analysis on all modules"
    dependsOn(subprojects.map { "${it.path}:detekt" })
}

tasks.register("codeQualityCheck") {
    group = "verification"
    description = "Run all code quality checks (ktlint + detekt)"
    dependsOn("ktlintCheckAll", "detektCheckAll")
}

tasks.register("codeQualityFormatAndCheck") {
    group = "verification"
    description = "Format code and run all quality checks (ktlint format + ktlint check + detekt)"
    dependsOn("ktlintFormatAll", "ktlintCheckAll", "detektCheckAll")

    // Ensure formatting runs before checks
    tasks.findByName("ktlintCheckAll")?.mustRunAfter("ktlintFormatAll")
    tasks.findByName("detektCheckAll")?.mustRunAfter("ktlintFormatAll")
}

