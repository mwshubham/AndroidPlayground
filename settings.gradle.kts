pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // JitPack — required for ANR-WatchDog (com.github.anrwatchdog:anrwatchdog)
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Android Playground"
include(":app")

// Feature modules
include(":feature:feed:api")
include(":feature:feed:impl")
include(":feature:image-upload:api")
include(":feature:image-upload:impl")
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:note:api")
include(":feature:note:impl")
include(":feature:media-orchestrator:api")
include(":feature:media-orchestrator:impl")
include(":feature:user-initiated-service:api")
include(":feature:user-initiated-service:impl")
include(":feature:crypto-security:api")
include(":feature:crypto-security:impl")
include(":feature:room-database:api")
include(":feature:room-database:impl")
include(":feature:inter-app-comm:api")
include(":feature:inter-app-comm:impl")

// Core modules
include(":core:analytics")
include(":core:common")
include(":core:navigation")
include(":core:ui")

