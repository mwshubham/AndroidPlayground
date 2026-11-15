---
name: android-code-review
description: "Review Kotlin, Compose, and Gradle code in AndroidPlayground for compliance with project conventions: MVI pattern (3 separate files), Screen/Content split, custom Preview annotations, Clean Architecture boundaries, Timber logging, ktlint, and detekt. Use when auditing code quality, reviewing pull requests or diffs, or verifying architectural compliance."
metadata:
  author: AndroidPlayground
  keywords:
    - android
    - code-review
    - compose
    - mvi
    - detekt
    - ktlint
    - architecture
---

## Overview

This skill audits Kotlin and Jetpack Compose code in **AndroidPlayground** against the project's canonical conventions. It is designed to be executed by any AI agent (GitHub Copilot, Android Studio Antigravity, Claude Code, Cursor, or CI automation).

The canonical rules are defined in `docs/conventions/` and serve as the **Single Source of Truth (SSOT)**:
- [MVI Pattern](file:///docs/conventions/mvi-pattern.md)
- [Compose Screen / Content Split](file:///docs/conventions/compose-screens.md)
- [UI Components & Previews](file:///docs/conventions/ui-components.md)
- [Feature Module Structure](file:///docs/conventions/feature-module-structure.md)
- [Code Quality, Logging & Clean Architecture](file:///docs/conventions/code-quality-and-logging.md)

---

## Step 1: Identify Review Targets & Verify Sibling Files

1. Determine target file(s) from user prompt, active editor context, or `git diff`.
2. Inspect the filesystem to verify mandatory sibling files exist:
   - **`*Screen.kt`**: A matching `*Content.kt` **must** exist in `presentation/component/`.
   - **`*Content.kt`**: A matching `*Screen.kt` **must** exist in `presentation/screen/`.
   - **MVI contracts**: All three sibling files (`*State.kt` in `presentation/state/`, `*Intent.kt` in `presentation/intent/`, `*SideEffect.kt` in `presentation/sideeffect/`) **must** exist. A combined `*Contract.kt` is forbidden.
   - **Feature `impl/`**: A matching `api/` module **must** exist under `feature/{name}/api/`.

---

## Step 2: Validate Against Canonical Rules

Evaluate the target files against these critical checklist categories:

### 1. MVI Pattern
- State is a `data class` with default values for every property. Pure data holder, no functions or business logic.
- Intent is a `sealed interface` (`data object` if parameterless, `data class` if parameterized).
- SideEffect is a `sealed interface` representing single-shot events (navigation, toast, snackbar).
- ViewModel is annotated with `@HiltViewModel`, exposes single `handleIntent(intent)`, uses `MutableStateFlow` (exposed as read-only `StateFlow`), `Channel.BUFFERED` for side-effects (exposed via `receiveAsFlow()`), and state updates via `_state.update { it.copy(...) }`.

### 2. Screen / Content Split
- **Screen file (`*Screen.kt`)**: Public composable. Only handles `hiltViewModel()`, `collectAsStateWithLifecycle()`, `LaunchedEffect` collecting side effects, and `TrackScreenViewEvent(screenName = "{Feature}Screen")`. Delegates 100% of UI layout to `*Content`. Must **not** contain `Scaffold`, `LazyColumn`, or UI layouts.
- **Content file (`*Content.kt`)**: Internal composable. Pure Compose UI taking `(state, onIntent, modifier)`. Must **not** access ViewModels or call `hiltViewModel()`. Must include at least one preview.

### 3. UI Components & Previews
- Every component lives in its own file under `presentation/component/`.
- Raw `@Preview` from `androidx.compose.ui.tooling.preview` is forbidden.
- Must use custom preview annotations from `com.example.android.playground.core.ui.preview`:
  `@ComponentPreview`, `@DualThemePreview`, `@PhonePreview`, or `@FullPreview`.
- Preview composables must be wrapped inside `PreviewContainer { ... }`, be `private`, and named `{ComponentName}Preview`.
- `modifier: Modifier = Modifier` must be the last parameter before trailing lambdas.

### 4. Logging & Clean Architecture
- `android.util.Log` is forbidden. Flag any call and any `import android.util.Log`. Use `timber.log.Timber`.
- Do not define manual `TAG` constants.
- Domain models from `domain/model/` must **never** be imported in `presentation/`. Must map via `{Entity}UiModel` and `{Entity}UiMapper`.
- Feature `impl` must never depend on another feature's `impl`. Only depend on `:feature:*:api`.
- No presentation-to-data leakage (all data requests go through domain use cases or repository interfaces).

### 5. API Module
- `feature/{name}/api` contains navigation routes in `{Name}Routes.kt`.
- Routes must be `@Serializable` data objects/classes implementing `NavKey`.
- No UI, no ViewModels, no business logic in `api/`. Dependencies limited to `androidx.navigation3.runtime` and `kotlinx.serialization.json`.

---

## Step 3: Execute Static Analysis & Formatting Checks

Run the project verification command:

```bash
./gradlew ktlintCheck detektCheckAll 2>&1 | grep -E "\.kt:[0-9]+|FAILED|BUILD SUCCESS"
```

- If `ktlintCheck` fails: Note that `./gradlew ktlintFormat` will auto-fix formatting.
- If `detekt` fails: Detail the exact code modifications required. Do not propose baseline suppressions.

---

## Step 4: Format Output

Structure your review response using this standard schema:

```markdown
## Review: {FileName or Feature}

### ✅ Passed
- [List verified rules that complied with conventions]

### ❌ Violations

#### {Rule Category}
- 🔴 **{File:Line}**: {Description of violation} → **Fix**: {Concrete fix}
- 🟡 **{File:Line}**: {Description of violation} → **Fix**: {Concrete fix}

### Summary
{N} error(s), {M} warning(s) across {K} file(s).
```

Severity Guide:
- 🔴 **Error**: Architectural breach, forbidden API (`android.util.Log`), combined contract, missing required sibling file, or CI failure.
- 🟡 **Warning**: Minor stylistic deviation, missing preview variant, suboptimal parameter ordering.
