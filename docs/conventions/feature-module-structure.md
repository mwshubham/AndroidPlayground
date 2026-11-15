# Feature Module Structure

This document serves as the canonical source of truth for creating and organizing feature modules in **AndroidPlayground**.

---

## Architecture: The `api` and `impl` Split

Every feature module lives under `feature/{name}/` and is strictly partitioned into two separate Gradle subprojects:

```
feature/{name}/
├── api/                  → Public contract: Navigation routes, exposed interfaces, shared models
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/example/android/playground/{name}/api/
│       └── {Name}Routes.kt          # NavKey navigation route objects
│
└── impl/                 → Full implementation: presentation, domain, data, DI
    ├── build.gradle.kts
    └── src/main/kotlin/com/example/android/playground/{name}/
        ├── presentation/
        │   ├── screen/{Name}Screen.kt              # Public ViewModel wiring only
        │   ├── component/{Name}Content.kt          # Internal pure UI composable
        │   ├── state/{Name}State.kt                # Immutable UI state (data class)
        │   ├── intent/{Name}Intent.kt              # User intents (sealed interface)
        │   ├── sideeffect/{Name}SideEffect.kt      # Single-shot side effects (sealed interface)
        │   ├── model/{Entity}UiModel.kt            # UI model representation
        │   ├── mapper/{Entity}UiMapper.kt          # Domain → UI Model mapper
        │   └── viewmodel/{Name}ViewModel.kt        # @HiltViewModel
        ├── domain/
        │   ├── model/{Entity}.kt                   # Pure Kotlin domain model
        │   ├── repository/{Name}Repository.kt      # Interface
        │   └── usecase/{Action}{Entity}UseCase.kt  # Single-responsibility use case
        ├── data/
        │   ├── repository/{Name}RepositoryImpl.kt  # Repository implementation
        │   └── local/                              # (If local DB needed)
        │       ├── {Name}Database.kt
        │       ├── {Name}Dao.kt
        │       └── {Name}Entity.kt
        └── di/
            ├── {Name}Module.kt                     # Hilt @Module bindings
            └── {Name}NavigationModule.kt           # Navigation DI (if needed)
```

---

## 1. Creating the `api` Module

**`feature/{name}/api/build.gradle.kts`**:
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.android.playground.{name}.api"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.json)
}
```

**`feature/{name}/api/src/.../{Name}Routes.kt`**:
```kotlin
package com.example.android.playground.{name}.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object {Name}Route : NavKey

// For parameterized routes:
// @Serializable
// data class {Name}DetailRoute(val id: Long) : NavKey
```

**`api` Module Invariants:**
- Contains **no** UI composables, ViewModels, or business logic.
- Dependencies strictly limited to `androidx.navigation3.runtime` and `kotlinx.serialization.json`.

---

## 2. Creating the `impl` Module

**`feature/{name}/impl/build.gradle.kts`**:
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.android.playground.{name}"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
        consumerProguardFiles("consumer-rules.pro")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures { compose = true }
    hilt { enableAggregatingTask = true }
}

dependencies {
    implementation(project(":feature:{name}:api"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.navigation3.compose)
    implementation(libs.androidx.navigation3.runtime)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
```

---

## 3. Registration in Root Files

1. In `settings.gradle.kts`:
   ```kotlin
   include(":feature:{name}:api")
   include(":feature:{name}:impl")
   ```
2. In `app/build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(project(":feature:{name}:impl"))
   }
   ```
3. Wire route into `app/src/main/kotlin/.../navigation/AppNavigation.kt`.
