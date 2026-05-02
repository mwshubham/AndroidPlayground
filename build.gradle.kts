import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
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

    // Load the custom rule JAR so ForbiddenAndroidLogCallRule runs on every module.
    // detektPlugins creates a proper compile+runtime dependency on the rule JAR.
    dependencies {
        add(
            "detektPlugins",
            files(rootProject.project(":custom-detekt-rules").layout.buildDirectory.file("libs/custom-detekt-rules.jar")),
        )
    }

    // Ensure the custom rules JAR is built before any detekt task in subprojects.
    tasks.withType<Detekt>().configureEach {
        dependsOn(rootProject.project(":custom-detekt-rules").tasks.named("jar"))
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

// Git hooks management tasks
tasks.register("installGitHooks") {
    group = "setup"
    description = "Install git pre-commit hook that runs code quality checks"

    doLast {
        val gitHooksDir = file(".git/hooks")
        if (!gitHooksDir.exists()) {
            throw GradleException("Git hooks directory not found. Make sure you're in a git repository.")
        }

        val preCommitHook = file(".git/hooks/pre-commit")
        val hookScriptFile = file("scripts/pre-commit-hook.sh")

        if (!hookScriptFile.exists()) {
            throw GradleException("Hook script not found at: ${hookScriptFile.absolutePath}")
        }

        // Copy the script content to the git hooks directory
        hookScriptFile.copyTo(preCommitHook, overwrite = true)


        // Make the hook executable (on Unix-like systems)
        if (!System.getProperty("os.name").lowercase().contains("windows")) {
            val process = ProcessBuilder("chmod", "+x", preCommitHook.absolutePath).start()
            process.waitFor()
        }

        logger.lifecycle("✅ Git pre-commit hook installed successfully!")
        logger.lifecycle("The hook will run ktlintFormat + ktlintCheck + detektCheckAll before each commit.")
        logger.lifecycle("To uninstall, run: ./gradlew uninstallGitHooks")
    }
}

tasks.register("uninstallGitHooks") {
    group = "setup"
    description = "Remove the git pre-commit hook"

    doLast {
        val preCommitHook = file(".git/hooks/pre-commit")
        if (preCommitHook.exists()) {
            preCommitHook.delete()
            logger.lifecycle("✅ Git pre-commit hook removed successfully!")
        } else {
            logger.lifecycle("ℹ️ No pre-commit hook found to remove.")
        }
    }
}

tasks.register("checkGitHooks") {
    group = "setup"
    description = "Check if git hooks are installed and show their status"

    doLast {
        val gitHooksDir = file(".git/hooks")
        val preCommitHook = file(".git/hooks/pre-commit")

        logger.lifecycle("Git Hooks Status:")
        logger.lifecycle("=================")

        if (!gitHooksDir.exists()) {
            logger.error("❌ Git hooks directory not found. Make sure you're in a git repository.")
            return@doLast
        }

        if (preCommitHook.exists()) {
            val content = preCommitHook.readText()
            if (content.contains("ktlintCheck") && content.contains("detektCheckAll")) {
                logger.lifecycle("✅ Pre-commit hook is installed and configured for code quality checks")

                // Check if hook is executable
                if (!System.getProperty("os.name").lowercase().contains("windows")) {
                    val isExecutable = preCommitHook.canExecute()
                    if (isExecutable) {
                        logger.lifecycle("✅ Hook is executable")
                    } else {
                        logger.warn("⚠️ Hook exists but is not executable. Run: chmod +x .git/hooks/pre-commit")
                    }
                }
            } else {
                logger.warn("⚠️ Pre-commit hook exists but doesn't contain code quality checks")
                logger.warn("   You may have a custom hook. Check .git/hooks/pre-commit")
            }
        } else {
            logger.error("❌ No pre-commit hook installed")
            logger.lifecycle("   Run './gradlew installGitHooks' to install")
        }

        logger.lifecycle("")
        logger.lifecycle("Available commands:")
        logger.lifecycle("  ./gradlew installGitHooks   - Install the pre-commit hook")
        logger.lifecycle("  ./gradlew uninstallGitHooks - Remove the pre-commit hook")
        logger.lifecycle("  ./gradlew checkGitHooks     - Check hook status")
    }
}

// Optional: Run hook installation automatically on first build
tasks.register("setupProject") {
    group = "setup"
    description = "Set up the project (install git hooks and run initial checks)"
    dependsOn("installGitHooks", "codeQualityFormatAndCheck")

    doLast {
        logger.lifecycle("")
        logger.lifecycle("🎉 Project setup complete!")
        logger.lifecycle("Git pre-commit hook is now active and will run code quality checks before each commit.")
    }
}
