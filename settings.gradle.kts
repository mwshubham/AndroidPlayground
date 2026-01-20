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
    }
}

rootProject.name = "Android Playground"
include(":app")

// Feature modules
include(":feature:feed")
include(":feature:image-upload")
include(":feature:login")
include(":feature:note")

// Core modules
include(":core:analytics")
include(":core:common")
include(":core:navigation")
include(":core:ui")

