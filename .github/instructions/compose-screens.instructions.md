---
description: "Use when creating or editing a Compose screen — any file named *Screen.kt or *Content.kt in a presentation layer. Covers the mandatory Screen/Content split pattern, ViewModel wiring, side-effect collection, and preview requirements."
applyTo: "**/presentation/screen/**/*.kt"
---

# Compose Screen / Content Pattern

## Rule: Always Split a Screen into Two Files

Every feature screen **must** be split across exactly two files:

| File | Package | Visibility | Responsibility |
|------|---------|------------|----------------|
| `{Feature}Screen.kt` | `presentation/screen/` | `public` | ViewModel wiring only |
| `{Feature}Content.kt` | `presentation/component/` | `internal` | Pure Compose UI |

**Never** put `Scaffold`, `LazyColumn`, or any layout composable directly inside the Screen composable. **Never** call `hiltViewModel()` or `collectAsStateWithLifecycle()` inside Content.

---

## Screen Template (`{Feature}Screen.kt`)

```kotlin
package com.example.android.playground.{feature}.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.{feature}.presentation.component.{Feature}Content
import com.example.android.playground.{feature}.presentation.intent.{Feature}Intent
import com.example.android.playground.{feature}.presentation.sideeffect.{Feature}SideEffect
import com.example.android.playground.{feature}.presentation.viewmodel.{Feature}ViewModel

@Composable
fun {Feature}Screen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: {Feature}ViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenViewEvent(screenName = "{Feature}Screen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is {Feature}SideEffect.NavigateBack -> onNavigateBack()
                // handle other side effects here
            }
        }
    }

    {Feature}Content(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}
```

**Checklist for Screen files:**
- [ ] Has `hiltViewModel()` call
- [ ] Has `collectAsStateWithLifecycle()` state collection
- [ ] Has `LaunchedEffect` for side-effect collection (if the feature has side effects)
- [ ] Has `TrackScreenViewEvent(screenName = "...")` for analytics
- [ ] Delegates **all UI** to the matching `{Feature}Content` composable
- [ ] Contains **no** `Scaffold`, `Column`, `LazyColumn`, or other layout composables

---

## Content Template (`{Feature}Content.kt`)

```kotlin
package com.example.android.playground.{feature}.presentation.component

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.{feature}.presentation.intent.{Feature}Intent
import com.example.android.playground.{feature}.presentation.state.{Feature}State

@Composable
internal fun {Feature}Content(
    state: {Feature}State,
    onIntent: ({Feature}Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "{Feature Title}",
                onNavigationClick = { onIntent({Feature}Intent.NavigateBack) },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        // UI content here
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun {Feature}ContentPreview() {
    PreviewContainer {
        {Feature}Content(
            state = {Feature}State(),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, name = "{Feature}Content - Loading")
@Composable
private fun {Feature}ContentLoadingPreview() {
    PreviewContainer {
        {Feature}Content(
            state = {Feature}State(isLoading = true),
            onIntent = {},
        )
    }
}
```

**Checklist for Content files:**
- [ ] Marked `internal`
- [ ] Accepts `state: {Feature}State` and `onIntent: ({Feature}Intent) -> Unit`
- [ ] Contains `Scaffold` (or other top-level layout)
- [ ] Has **no** ViewModel dependency, no `hiltViewModel()`, no `collectAsStateWithLifecycle()`
- [ ] Has at least one `@Preview` at the bottom (prefer `@ComponentPreview` from `core.ui.preview`)
- [ ] Preview functions are `private`

---

## Real-World Reference

See `feature/media-orchestrator/impl/src/main/kotlin/com/example/android/playground/mediaorchestrator/presentation/` for a complete correct implementation:
- `screen/MediaOrchestratorScreen.kt` — ViewModel wiring
- `component/MediaOrchestratorContent.kt` — pure UI with previews
