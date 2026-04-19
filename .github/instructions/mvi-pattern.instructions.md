---
description: "Use when implementing MVI (Model-View-Intent) pattern, creating State, Intent, SideEffect, or ViewModel classes for any feature screen. Covers file locations, naming, class shapes, and ViewModel boilerplate."
applyTo: "**/presentation/**/*.kt"
---

# MVI Pattern

## Rule: Three Separate Files — Never a Combined Contract

MVI contracts are **always** split into three separate files. A single `*Contract.kt` combining all three is **forbidden**.

| Class | File Location | Kind |
|-------|--------------|------|
| `{Feature}State` | `presentation/state/{Feature}State.kt` | `data class` |
| `{Feature}Intent` | `presentation/intent/{Feature}Intent.kt` | `sealed interface` |
| `{Feature}SideEffect` | `presentation/sideeffect/{Feature}SideEffect.kt` | `sealed interface` |
| `{Feature}ViewModel` | `presentation/viewmodel/{Feature}ViewModel.kt` | `@HiltViewModel class` |

---

## State Template

State is a plain `data class` with default values for every field so that `{Feature}State()` (no args) always represents the initial/empty state — this is required for previews.

```kotlin
// presentation/state/{Feature}State.kt
package com.example.android.playground.{feature}.presentation.state

data class {Feature}State(
    val isLoading: Boolean = false,
    val error: String? = null,
    // add feature-specific fields here
)
```

**Rules:**
- Every field **must** have a default value
- `{Feature}State()` must be a valid preview state
- No business logic, no functions — pure data holder
- Use UI models (`{Entity}UiModel`) for list items, not domain models

---

## Intent Template

Intent is a `sealed interface` where each user action or event is a nested type.

```kotlin
// presentation/intent/{Feature}Intent.kt
package com.example.android.playground.{feature}.presentation.intent

sealed interface {Feature}Intent {
    data object LoadData : {Feature}Intent
    data object NavigateBack : {Feature}Intent
    data class OnItemClicked(val id: Long) : {Feature}Intent
    // add more intents as needed
}
```

**Rules:**
- Use `data object` for parameterless intents
- Use `data class` for intents with parameters
- Intent names describe **user actions** or **events** (not commands): `OnItemClicked`, `OnRetryTapped`, `LoadData`
- No business logic here

---

## SideEffect Template

SideEffect is a `sealed interface` for **one-time events** that do not belong in state (navigation, snackbars, toasts).

```kotlin
// presentation/sideeffect/{Feature}SideEffect.kt
package com.example.android.playground.{feature}.presentation.sideeffect

sealed interface {Feature}SideEffect {
    data object NavigateBack : {Feature}SideEffect
    data class NavigateTo(val id: Long) : {Feature}SideEffect
    data class ShowMessage(val message: String) : {Feature}SideEffect
}
```

**Rules:**
- Only use for one-time events; repeated/persistent state belongs in `{Feature}State`
- Always include a `NavigateBack` if the screen can navigate back
- `ShowMessage` / `ShowSnackbar` for snackbars/toasts

---

## ViewModel Template

```kotlin
// presentation/viewmodel/{Feature}ViewModel.kt
package com.example.android.playground.{feature}.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.android.playground.{feature}.presentation.intent.{Feature}Intent
import com.example.android.playground.{feature}.presentation.sideeffect.{Feature}SideEffect
import com.example.android.playground.{feature}.presentation.state.{Feature}State
import javax.inject.Inject

@HiltViewModel
class {Feature}ViewModel @Inject constructor(
    // inject use cases here
) : ViewModel() {

    private val _state = MutableStateFlow({Feature}State())
    val state: StateFlow<{Feature}State> = _state.asStateFlow()

    private val _sideEffect = Channel<{Feature}SideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: {Feature}Intent) {
        when (intent) {
            is {Feature}Intent.NavigateBack -> sendEffect({Feature}SideEffect.NavigateBack)
            is {Feature}Intent.LoadData -> loadData()
            // handle other intents
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // call use case
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: {Feature}SideEffect) {
        viewModelScope.launch { _sideEffect.send(effect) }
    }
}
```

**Rules:**
- State via `MutableStateFlow` + `asStateFlow()` — never expose mutable state
- Side effects via `Channel(Channel.BUFFERED)` + `receiveAsFlow()` — never `SharedFlow`
- Single `handleIntent(intent: {Feature}Intent)` entry point for all intents
- Use `_state.update { }` (not `_state.value = ...`) to avoid race conditions

---

## Real-World References

Correct implementations to follow:
- `feature/note/impl/.../presentation/state/NoteListState.kt`
- `feature/note/impl/.../presentation/intent/NoteListIntent.kt`
- `feature/note/impl/.../presentation/sideeffect/NoteListSideEffect.kt`
- `feature/media-orchestrator/impl/.../presentation/viewmodel/MediaOrchestratorViewModel.kt`

**Anti-pattern** (do NOT replicate — scheduled for migration):
- `feature/feed/impl/.../presentation/FeedContract.kt` — combines all three in one file
- `feature/login/impl/.../presentation/LoginContract.kt` — same anti-pattern
