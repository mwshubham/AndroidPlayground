# AndroidPlayground — Project Guidelines

## Architecture

This project uses **Clean Architecture** with **modular feature modules**.

```
app/                    → Thin shell: navigation setup, Application class, DI graph root
core/
  ui/                   → Shared Compose theme, reusable components, preview utilities
  common/               → Common extensions, base classes shared across features
  navigation/           → Navigation graph setup & animations
  analytics/            → Analytics event tracking
feature/{name}/
  api/                  → Public contracts: interfaces, navigation routes, exposed models
  impl/                 → Full implementation: presentation, domain, data, DI
```

Each feature impl module always has four layers:

```
presentation/   → Compose UI (screens, components, ViewModels)
domain/         → Use cases, repository interfaces, domain models
data/           → Repository implementations, data sources, local DB
di/             → Hilt modules wiring the above
```

**Dependency rule**: `presentation` → `domain` ← `data`

## Tech Stack

- **UI**: Jetpack Compose + Material 3
- **Pattern**: MVI (Model-View-Intent) with `StateFlow` + `Channel`
- **DI**: Hilt
- **Build**: Gradle Kotlin DSL, version catalog at `gradle/libs.versions.toml`
- **Navigation**: `core/navigation` with type-safe routes
- **Code quality**: ktlint (formatting), Detekt (static analysis)
- **Logging**: Timber (`com.jakewharton.timber:timber`) — see rule below
- **Testing**: JUnit 4, Turbine, MockK

## Package Naming

Base package: `com.example.android.playground.{featureName}`

Feature package example: `com.example.android.playground.mediaorchestrator`

## Presentation Layer Rules

### 1 — Screen / Content Split (always)

Every screen **must** be split across two separate files:

| File | Location | Rule |
|------|----------|------|
| `{Feature}Screen.kt` | `presentation/screen/` | Public entry point. Holds `hiltViewModel()`, `collectAsStateWithLifecycle()`, `LaunchedEffect` for side-effects, and delegates all UI to `{Feature}Content`. No `Scaffold` or layout here. |
| `{Feature}Content.kt` | `presentation/component/` | Internal pure-Compose composable. Accepts `state` and `onIntent` lambda. No ViewModel, no `hiltViewModel()`. Has its own `@Preview`. |

See `.github/instructions/compose-screens.instructions.md` for the full template.

### 2 — MVI: State, Intent, SideEffect in separate files (always)

MVI contracts are **never** combined into a single `*Contract.kt` file.

| Class | Location | Kind |
|-------|----------|------|
| `{Feature}State` | `presentation/state/{Feature}State.kt` | `data class` |
| `{Feature}Intent` | `presentation/intent/{Feature}Intent.kt` | `sealed interface` |
| `{Feature}SideEffect` | `presentation/sideeffect/{Feature}SideEffect.kt` | `sealed interface` |

See `.github/instructions/mvi-pattern.instructions.md` for the full template.

### 3 — UI Components: one file, one preview (always)

Every UI component lives in its **own file** under `presentation/component/`.  
Every component file **must** include at least one `@Preview` composable at the bottom.  
Use the custom preview annotations from `core.ui.preview` — **not** raw `@Preview`:

```kotlin
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
```

See `.github/instructions/ui-components.instructions.md` for the full template.

## Naming Conventions

| Item | Pattern | Example |
|------|---------|---------|
| State | `{Feature}State` | `NoteListState` |
| Intent | `{Feature}Intent` | `NoteListIntent` |
| SideEffect | `{Feature}SideEffect` | `NoteListSideEffect` |
| ViewModel | `{Feature}ViewModel` | `NoteListViewModel` |
| Screen composable | `{Feature}Screen` | `NoteListScreen` |
| Content composable | `{Feature}Content` | `NoteListContent` |
| UI model | `{Entity}UiModel` | `NoteListItemUiModel` |
| Mapper | `{Entity}UiMapper` | `NoteUiMapper` |
| Component | descriptive noun | `TopicCard`, `StatusChip` |
| Preview fn | `{Component}Preview` or named variants | `TopicCardPreview`, `@Preview(name = "Expanded")` |

## Build & Quality Commands

```bash
./gradlew ktlintCheck          # check formatting
./gradlew ktlintFormat         # auto-fix formatting
./gradlew detekt               # static analysis
./gradlew test                 # unit tests
./gradlew installGitHooks      # install pre-commit hook (run once after clone)
```

## Copilot Agent Rules

After editing **any** `.kt` or `.kts` file, always run the following and fix every violation before considering the task done:

```bash
./gradlew ktlintCheck detekt 2>&1 | grep -E "\.kt:[0-9]+|FAILED|BUILD SUCCESS"
```

- If `ktlintCheck` reports violations, run `./gradlew ktlintFormat` then re-run the check.
- If `detekt` reports violations, fix them in code — do **not** add baseline suppressions unless explicitly asked.
- Never leave a task in a state that would fail CI.

## Adding a New Feature Module

See `.github/instructions/feature-module-structure.instructions.md` and [MODULE_STRUCTURE.md](../MODULE_STRUCTURE.md).

## Logging Rules

**Never** use `android.util.Log` in any Kotlin file. **Always** use `timber.log.Timber`.

| Forbidden | Use instead |
|-----------|-------------|
| `Log.d(TAG, msg)` | `Timber.d(msg)` |
| `Log.e(TAG, msg)` | `Timber.e(msg)` |
| `Log.i(TAG, msg)` | `Timber.i(msg)` |
| `Log.w(TAG, msg)` | `Timber.w(msg)` |
| `Log.v(TAG, msg)` | `Timber.v(msg)` |
| `Log.wtf(TAG, msg)` | `Timber.wtf(msg)` |

- Do **not** import `android.util.Log` — not even unused.
- Do **not** define a `TAG` constant for `android.util.Log`.
- Timber is already in the version catalog as `libs.timber`; add it to a module's `build.gradle.kts` with `implementation(libs.timber)` when logging is needed.
- Timber handles the tag automatically from the calling class name — no manual `TAG` needed.
- This rule is enforced by Detekt (`ForbiddenImport`, `ForbiddenMethodCall`) and will fail CI if violated.
