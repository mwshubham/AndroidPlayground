import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.artifacts.ProjectDependency
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
            sarif.required.set(true)
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

// Generates JaCoCo XML coverage reports for all Android library modules that
// have enableUnitTestCoverage = true in their debug buildType.
// Each qualifying module emits: build/reports/coverage/test/debug/report.xml
//
// Uses gradle.projectsEvaluated so all subprojects are fully configured before
// wiring dependencies — the only reliable hook after AGP has registered all its
// tasks lazily, including createDebugUnitTestCoverageReport.
tasks.register("coverageReport") {
    group = "verification"
    description = "Generate JaCoCo unit test coverage reports for all modules"
}

gradle.projectsEvaluated {
    val coverageTask = rootProject.tasks.named("coverageReport")
    subprojects.forEach { sub ->
        val isFeatureImpl = sub.path.startsWith(":feature:") && sub.path.endsWith(":impl")
        val isCore = sub.path.startsWith(":core:")
        if (isFeatureImpl || isCore) {
            // Only wire coverage for modules that actually have unit test sources.
            // createDebugUnitTestCoverageReport throws with "no coverage data found"
            // when no tests were run — i.e., the module has an empty src/test tree.
            val hasTestSources = sub.file("src/test").walkTopDown()
                .any { it.isFile && it.extension == "kt" }
            if (hasTestSources) {
                sub.tasks.findByName("createDebugUnitTestCoverageReport")?.let { perModuleTask ->
                    coverageTask.configure { dependsOn(perModuleTask) }
                }
            }
        }
    }
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

// ─────────────────────────────────────────────────────────────────────────────
// Module Dependency Graph
// Generates docs/module-graph.md with a Mermaid diagram of all inter-module
// dependencies. Run ./gradlew generateModuleGraph and commit the result whenever
// module wiring changes. CI verifies the committed file is current.
// ─────────────────────────────────────────────────────────────────────────────
tasks.register("generateModuleGraph") {
    group = "documentation"
    description = "Generate a Mermaid module dependency graph at docs/module-graph.md"

    doLast {
        val edges = mutableSetOf<Pair<String, String>>()

        subprojects.forEach { proj ->
            listOf("implementation", "api", "runtimeOnly").forEach { configName ->
                proj.configurations.findByName(configName)
                    ?.dependencies
                    ?.filterIsInstance<ProjectDependency>()
                    ?.forEach { dep ->
                        edges.add(proj.path to dep.path)
                    }
            }
        }

        val allNodes = (edges.map { it.first } + edges.map { it.second }).toSortedSet()

        fun String.toNodeId() = removePrefix(":").replace(":", "_").replace("-", "_")
        fun String.toNodeLabel() = removePrefix(":").replace(":", "/")

        val content = buildString {
            appendLine("# Module Dependency Graph")
            appendLine()
            appendLine("_Auto-generated. Run `./gradlew generateModuleGraph` and commit the result to update._")
            appendLine()
            appendLine("```mermaid")
            appendLine("graph LR")
            allNodes.forEach { node ->
                appendLine("    ${node.toNodeId()}[\"${node.toNodeLabel()}\"]")
            }
            appendLine()
            edges.sortedWith(compareBy({ it.first }, { it.second })).forEach { (from, to) ->
                appendLine("    ${from.toNodeId()} --> ${to.toNodeId()}")
            }
            appendLine("```")
        }

        val outputFile = file("docs/module-graph.md")
        outputFile.parentFile.mkdirs()
        outputFile.writeText(content)
        logger.lifecycle("Module graph written to ${outputFile.relativeTo(rootDir).path}")
    }
}
