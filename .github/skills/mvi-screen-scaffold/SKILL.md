---
name: mvi-screen-scaffold
description: "Scaffold a complete MVI feature screen in AndroidPlayground: creates {Feature}State.kt, {Feature}Intent.kt, {Feature}SideEffect.kt, {Feature}ViewModel.kt, {Feature}Screen.kt, and {Feature}Content.kt following the project's strict architecture conventions."
metadata:
  author: AndroidPlayground
  keywords:
    - mvi
    - compose
    - viewmodel
    - screen-scaffold
---

## Overview

In **AndroidPlayground**, every screen is strictly composed of **6 separate files** across dedicated packages. A single combined contract file is strictly forbidden.

When requested to scaffold or generate a new screen for `{Feature}` in `com.example.android.playground.{feature}`, generate the following 6 files:

```
feature/{feature}/impl/src/main/kotlin/com/example/android/playground/{feature}/presentation/
├── state/{Feature}State.kt
├── intent/{Feature}Intent.kt
├── sideeffect/{Feature}SideEffect.kt
├── viewmodel/{Feature}ViewModel.kt
├── screen/{Feature}Screen.kt
└── component/{Feature}Content.kt
```

---

## 1. State: `presentation/state/{Feature}State.kt`

```kotlin
package com.example.android.playground.{feature}.presentation.state

data class {Feature}State(
    val isLoading: Boolean = false,
    val error: String? = null,
    // Add feature fields here; every field MUST have a default value
)
```

**Invariants**:
- Plain `data class` with default values for every parameter (no-arg constructor must be valid).
- Pure data holder, no business logic or functions.
- Uses UI models (`{Entity}UiModel`), never domain models directly.

---

## 2. Intent: `presentation/intent/{Feature}Intent.kt`

```kotlin
package com.example.android.playground.{feature}.presentation.intent

sealed interface {Feature}Intent {
    data object OnScreenStarted : {Feature}Intent
    data object OnNavigateBackClicked : {Feature}Intent
    // Use data object for parameterless intents
    // Use data class for parameterized intents (e.g., data class OnItemClicked(val id: String) : {Feature}Intent)
}
```

---

## 3. SideEffect: `presentation/sideeffect/{Feature}SideEffect.kt`

```kotlin
package com.example.android.playground.{feature}.presentation.sideeffect

sealed interface {Feature}SideEffect {
    data object NavigateBack : {Feature}SideEffect
    data class ShowSnackbar(val message: String) : {Feature}SideEffect
}
```

**Invariants**:
- Reserved strictly for transient single-shot actions (navigation, dialogs, toasts).

---

## 4. ViewModel: `presentation/viewmodel/{Feature}ViewModel.kt`

```kotlin
package com.example.android.playground.{feature}.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.playground.{feature}.presentation.intent.{Feature}Intent
import com.example.android.playground.{feature}.presentation.sideeffect.{Feature}SideEffect
import com.example.android.playground.{feature}.presentation.state.{Feature}State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class {Feature}ViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow({Feature}State())
    val state: StateFlow<{Feature}State> = _state.asStateFlow()

    private val _sideEffect = Channel<{Feature}SideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: {Feature}Intent) {
        when (intent) {
            is {Feature}Intent.OnScreenStarted -> loadInitialData()
            is {Feature}Intent.OnNavigateBackClicked -> emitSideEffect({Feature}SideEffect.NavigateBack)
        }
    }

    private fun loadInitialData() {
        _state.update { it.copy(isLoading = true, error = null) }
        // Perform use case dispatch here
    }

    private fun emitSideEffect(effect: {Feature}SideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
```

---

## 5. Screen: `presentation/screen/{Feature}Screen.kt`

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
                is {Feature}SideEffect.ShowSnackbar -> { /* Handle snackbar */ }
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

---

## 6. Content: `presentation/component/{Feature}Content.kt`

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
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(text = state.error)
                else -> Text(text = "{Feature} Content")
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

---

## Verification
After generating the 6 files, run:
```bash
./gradlew ktlintCheck detektCheckAll 2>&1 | grep -E "\.kt:[0-9]+|FAILED|BUILD SUCCESS"
```
