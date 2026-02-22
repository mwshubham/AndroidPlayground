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
include(":feature:feed:api")
include(":feature:feed:impl")
include(":feature:image-upload:api")
include(":feature:image-upload:impl")
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:note:api")
include(":feature:note:impl")

// Core modules
include(":core:analytics")
include(":core:common")
include(":core:navigation")
include(":core:ui")

