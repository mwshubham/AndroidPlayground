# Compose Screen / Content Split Pattern

This document serves as the canonical source of truth for creating and maintaining screen composables in **AndroidPlayground**.

---

## Rule: Mandatory Screen / Content Split

Every screen feature **must** be split across exactly two separate files:

| File | Package Location | Visibility | Responsibility |
|---|---|---|---|
| `{Feature}Screen.kt` | `presentation/screen/` | `public` | ViewModel wiring, state/effect collection, analytics only |
| `{Feature}Content.kt` | `presentation/component/` | `internal` | Pure Compose UI and layouts, previews |

**Critical Constraints:**
- **Never** put `Scaffold`, `LazyColumn`, `Column`, or layout composables directly in the Screen file.
- **Never** call `hiltViewModel()` or `collectAsStateWithLifecycle()` inside the Content file.

---

## 1. Screen Template (`{Feature}Screen.kt`)

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
                // Handle remaining side effects
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

**Screen Checklist:**
- [ ] Marked `public`.
- [ ] Obtains ViewModel via `hiltViewModel()`.
- [ ] Collects state with `collectAsStateWithLifecycle()`.
- [ ] Collects side effects in `LaunchedEffect(viewModel)`.
- [ ] Calls `TrackScreenViewEvent(screenName = "{Feature}Screen")` for analytics tracking.
- [ ] Delegates all rendering to `{Feature}Content`.
- [ ] Contains **zero** layout composables (`Scaffold`, `Column`, etc.).

---

## 2. Content Template (`{Feature}Content.kt`)

```kotlin
package com.example.android.playground.{feature}.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.DualThemePreview
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
                onNavigationClick = { onIntent({Feature}Intent.OnNavigateBackClicked) },
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(text = state.error)
                else -> Text(text = "Content for {Feature}")
            }
        }
    }
}

// ---- Previews ----

@DualThemePreview
@Composable
private fun {Feature}ContentPreview() {
    PreviewContainer {
        {Feature}Content(
            state = {Feature}State(),
            onIntent = {},
        )
    }
}
```

**Content Checklist:**
- [ ] Marked `internal`.
- [ ] Accepts `(state: {Feature}State, onIntent: ({Feature}Intent) -> Unit, modifier: Modifier)`.
- [ ] Contains `Scaffold` and full layout hierarchy.
- [ ] **No** ViewModel dependency, no `hiltViewModel()`, no `collectAsStateWithLifecycle()`.
- [ ] Includes `@Preview` wrapped in `PreviewContainer { ... }`.
- [ ] Preview function is `private` and named `{Feature}ContentPreview`.
