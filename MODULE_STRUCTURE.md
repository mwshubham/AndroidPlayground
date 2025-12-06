# Android System Design Modular Architecture

This project demonstrates various system design concepts implemented as separate Android modules with a clean modular architecture for better separation of concerns and maintainability.

## Updated Project Structure (December 2025)

```
AndroidSystemDesign/
├── app/                          # Main application module
│   ├── src/main/java/com/example/android/systemdesign/
│   │   ├── MainActivity.kt       # Main activity with navigation setup
│   │   ├── SystemDesignApplication.kt
│   │   ├── presentation/         # App-specific presentation layer
│   │   └── di/                   # App-level dependency injection
│   └── build.gradle.kts
├── core/                         # Core modules directory
│   ├── domain/                   # Domain layer (business logic)
│   │   ├── model/               # Domain models
│   │   ├── repository/          # Repository interfaces  
│   │   └── usecase/             # Use cases
│   ├── data/                    # Data layer
│   │   ├── repository/          # Repository implementations
│   │   └── di/                  # Data module DI
│   ├── ui/                      # Shared UI components and theme
│   │   ├── theme/               # App theme (colors, typography)
│   │   └── components/          # Reusable UI components
│   └── navigation/              # Navigation components
│       ├── AppNavigation.kt     # Main navigation setup
│       └── NavigationAnimations.kt
├── feature/                     # Feature modules directory
│   ├── image-upload/            # Image Upload System Design Module
│   │   ├── src/main/java/com/example/android/systemdesign/imageupload/
│   │   ├── presentation/        # UI layer (Screens, ViewModels)
│   │   ├── domain/              # Business logic (Use cases, Models, Repositories)
│   │   ├── data/                # Data layer (Repository implementations)
│   │   └── di/                  # Module-specific dependency injection
│   └── build.gradle.kts
└── settings.gradle.kts
```

## Modules

### 1. Image Upload Module (`imageupload`)
Demonstrates image upload functionality with clean architecture principles.

**Features:**
- Image selection from gallery/camera
- Upload progress tracking
- Error handling
- Clean architecture implementation

**Components:**
- `ImageUploadScreen`: Main UI for image upload
- `ImageUploadViewModel`: Handles upload state and business logic
- `UploadImageUseCase`: Business logic for image uploading
- `ImageUploadRepository`: Abstract repository interface
- `ImageUploadRepositoryImpl`: Concrete repository implementation

## Adding New System Design Modules

To add a new system design module (e.g., chat system, notification system, etc.):

1. Create a new module directory:
   ```
   mkdir newsmodule
   mkdir -p newsmodule/src/main/java/com/example/android/systemdesign/newsmodule/{presentation,domain,data,di}
   ```

2. Create `build.gradle.kts` for the new module:
   ```kotlin
   plugins {
       alias(libs.plugins.android.library)
       alias(libs.plugins.kotlin.android)
       alias(libs.plugins.kotlin.compose)
       alias(libs.plugins.hilt.android)
       alias(libs.plugins.ksp)
   }
   
   android {
       namespace = "com.example.android.systemdesign.newsmodule"
       compileSdk = 35
       // ... rest of the configuration
   }
   
   dependencies {
       // Standard dependencies
   }
   ```

3. Add the module to `settings.gradle.kts`:
   ```kotlin
   include(":newsmodule")
   ```

4. Add the module dependency to the main app's `build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(project(":newsmodule"))
       // ... other dependencies
   }
   ```

5. Create the module structure:
   - **Presentation Layer**: UI components, screens, view models
   - **Domain Layer**: Use cases, repository interfaces, domain models
   - **Data Layer**: Repository implementations, data sources
   - **DI Layer**: Dependency injection configuration

6. Update navigation in the main app to include the new module's screens.

## Benefits of Modular Architecture

1. **Separation of Concerns**: Each module handles a specific system design concept
2. **Reusability**: Modules can be reused across different projects
3. **Testability**: Each module can be tested independently
4. **Build Performance**: Gradle can build modules in parallel
5. **Team Collaboration**: Different teams can work on different modules
6. **Feature Toggling**: Easy to enable/disable features by excluding modules

## Clean Architecture Principles

Each module follows Clean Architecture principles:

- **Presentation Layer**: UI components and ViewModels
- **Domain Layer**: Business logic, use cases, and repository interfaces
- **Data Layer**: Repository implementations and data sources

Dependencies flow inward: Presentation → Domain ← Data

## Development Guidelines

1. Keep modules focused on a single system design concept
2. Use dependency injection (Hilt) for managing dependencies
3. Follow MVVM pattern for UI components
4. Use Repository pattern for data access
5. Write unit tests for each module
6. Use Compose for UI development
7. Follow Material Design guidelines
