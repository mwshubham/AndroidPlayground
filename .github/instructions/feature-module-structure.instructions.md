---
description: "Use when creating a new feature module, adding a new screen to an existing feature, or scaffolding the full directory structure for a feature. Covers api/impl split, required files per layer, build.gradle.kts setup, and settings.gradle.kts registration."
---

# Feature Module Structure

## Directory Layout

Every feature lives under `feature/{name}/` and is split into two Gradle modules:

```
feature/{name}/
├── api/                  → Public contract (routes, exposed interfaces, shared models)
│   ├── build.gradle.kts
│   └── src/main/kotlin/com/example/android/playground/{name}/api/
│       └── {Name}Routes.kt          # NavKey route objects
│
└── impl/                 → Full implementation
    ├── build.gradle.kts
    └── src/main/kotlin/com/example/android/playground/{name}/
        ├── presentation/
        │   ├── screen/
        │   │   └── {Name}Screen.kt              # ViewModel wiring only
        │   ├── component/
        │   │   ├── {Name}Content.kt             # Pure UI, internal
        │   │   └── {ComponentName}.kt           # One file per component
        │   ├── state/
        │   │   └── {Name}State.kt               # data class
        │   ├── intent/
        │   │   └── {Name}Intent.kt              # sealed interface
        │   ├── sideeffect/
        │   │   └── {Name}SideEffect.kt          # sealed interface
        │   ├── model/
        │   │   └── {Entity}UiModel.kt           # UI representation of domain model
        │   ├── mapper/
        │   │   └── {Entity}UiMapper.kt          # Domain → UiModel mapping
        │   └── viewmodel/
        │       └── {Name}ViewModel.kt           # @HiltViewModel
        ├── domain/
        │   ├── model/
        │   │   └── {Entity}.kt                  # Domain model
        │   ├── repository/
        │   │   └── {Name}Repository.kt          # Interface
        │   └── usecase/
        │       └── {Action}{Entity}UseCase.kt   # Single-responsibility use case
        ├── data/
        │   ├── repository/
        │   │   └── {Name}RepositoryImpl.kt      # Repository implementation
        │   └── local/                           # (if DB needed)
        │       ├── {Name}Database.kt
        │       ├── {Name}Dao.kt
        │       └── {Name}Entity.kt
        └── di/
            ├── {Name}Module.kt                  # Hilt @Module bindings
            └── {Name}NavigationModule.kt        # Navigation DI (if needed)
```

---

## Step-by-Step: Creating a New Feature Module

### 1. Create the `api` module

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

**`{Name}Routes.kt`** (type-safe navigation keys):
```kotlin
package com.example.android.playground.{name}.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object {Name}Route : NavKey
```

### 2. Create the `impl` module

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

### 3. Register in `settings.gradle.kts`

```kotlin
include(":feature:{name}:api")
include(":feature:{name}:impl")
```

### 4. Add to `app/build.gradle.kts`

```kotlin
dependencies {
    implementation(project(":feature:{name}:impl"))
    // ...
}
```

### 5. Wire navigation in `app/`

Add the feature's route to the navigation graph in `app/src/main/kotlin/.../navigation/`.

---

## Required Files Checklist

For every new feature screen, confirm these files exist:

### Presentation
- [ ] `presentation/screen/{Name}Screen.kt` — ViewModel wiring, no layout
- [ ] `presentation/component/{Name}Content.kt` — pure UI, `internal`, has `@Preview`
- [ ] `presentation/state/{Name}State.kt` — `data class`, all fields have defaults
- [ ] `presentation/intent/{Name}Intent.kt` — `sealed interface`
- [ ] `presentation/sideeffect/{Name}SideEffect.kt` — `sealed interface`
- [ ] `presentation/viewmodel/{Name}ViewModel.kt` — `@HiltViewModel`
- [ ] Each reusable component → its own file in `presentation/component/` with `@Preview`

### Domain
- [ ] `domain/model/{Entity}.kt` — domain model
- [ ] `domain/repository/{Name}Repository.kt` — interface
- [ ] `domain/usecase/{Action}{Entity}UseCase.kt` — one use case per action

### Data
- [ ] `data/repository/{Name}RepositoryImpl.kt`

### DI
- [ ] `di/{Name}Module.kt` — `@Module @InstallIn(SingletonComponent::class)`

---

## Real-World Reference

- `feature/note/` — complete example with all layers, Room DB, and full MVI
- `feature/media-orchestrator/` — example with WorkManager integration
- `feature/user-initiated-service/` — example with foreground service
