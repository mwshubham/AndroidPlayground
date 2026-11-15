# MVI Pattern Architecture

This document serves as the canonical source of truth for the **MVI (Model-View-Intent)** architecture pattern in **AndroidPlayground**.

---

## Rule: Three Separate Files — Never a Combined Contract

MVI contracts are **always** split into three separate files across dedicated subpackages. A single `*Contract.kt` combining all three is **strictly forbidden**.

| Class | Required File Location | Kind |
|---|---|---|
| `{Feature}State` | `presentation/state/{Feature}State.kt` | `data class` |
| `{Feature}Intent` | `presentation/intent/{Feature}Intent.kt` | `sealed interface` |
| `{Feature}SideEffect` | `presentation/sideeffect/{Feature}SideEffect.kt` | `sealed interface` |
| `{Feature}ViewModel` | `presentation/viewmodel/{Feature}ViewModel.kt` | `@HiltViewModel class` |

---

## 1. State Template

State is a plain `data class` where **every field must have a default value**. This guarantees that `{Feature}State()` (invoked with zero arguments) produces a valid preview state.

```kotlin
// presentation/state/{Feature}State.kt
package com.example.android.playground.{feature}.presentation.state

data class {Feature}State(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<{Entity}UiModel> = emptyList(),
)
```

**State Invariants:**
- Plain `data class`, not a regular class or object.
- Every field **must** have a default value.
- No business logic or functions — pure immutable data holder.
- List items must use UI models (`{Entity}UiModel`), never domain models directly.

---

## 2. Intent Template

Intent is a `sealed interface` where each user action or UI event is declared.

```kotlin
// presentation/intent/{Feature}Intent.kt
package com.example.android.playground.{feature}.presentation.intent

sealed interface {Feature}Intent {
    data object OnScreenStarted : {Feature}Intent
    data object OnNavigateBackClicked : {Feature}Intent
    data class OnItemClicked(val id: Long) : {Feature}Intent
}
```

**Intent Invariants:**
- Must be a `sealed interface`.
- Parameterless intents must use `data object`.
- Parameterized intents must use `data class`.
- Intent names describe **user actions or events** (e.g., `OnItemClicked`, `OnRetryTapped`, `OnSearchQueryChanged`), not imperative commands.

---

## 3. SideEffect Template

SideEffect is a `sealed interface` for **one-time events** (e.g., navigation, snackbars, toasts).

```kotlin
// presentation/sideeffect/{Feature}SideEffect.kt
package com.example.android.playground.{feature}.presentation.sideeffect

sealed interface {Feature}SideEffect {
    data object NavigateBack : {Feature}SideEffect
    data class NavigateToDetail(val id: Long) : {Feature}SideEffect
    data class ShowSnackbar(val message: String) : {Feature}SideEffect
}
```

**SideEffect Invariants:**
- Must be a `sealed interface`.
- Reserved strictly for transient single-shot actions. Persistent or repeated UI state belongs in `{Feature}State`.

---

## 4. ViewModel Template

```kotlin
// presentation/viewmodel/{Feature}ViewModel.kt
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
import javax.inject.Inject

@HiltViewModel
class {Feature}ViewModel @Inject constructor(
    // Inject domain use cases or repositories here
) : ViewModel() {

    private val _state = MutableStateFlow({Feature}State())
    val state: StateFlow<{Feature}State> = _state.asStateFlow()

    private val _sideEffect = Channel<{Feature}SideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleIntent(intent: {Feature}Intent) {
        when (intent) {
            is {Feature}Intent.OnScreenStarted -> loadData()
            is {Feature}Intent.OnNavigateBackClicked -> sendEffect({Feature}SideEffect.NavigateBack)
            is {Feature}Intent.OnItemClicked -> { /* handle click */ }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // Execute use case
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: {Feature}SideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
```

**ViewModel Invariants:**
- Annotated with `@HiltViewModel` and `@Inject constructor(...)`.
- Exposes immutable state via `StateFlow<{Feature}State>` backed by `MutableStateFlow`.
- Exposes side-effects via `Channel<{Feature}SideEffect>(Channel.BUFFERED).receiveAsFlow()`. Never use `SharedFlow` for side effects.
- Single `fun handleIntent(intent: {Feature}Intent)` entry point for all intents.
- State mutation must use `_state.update { it.copy(...) }` — never `_state.value = ...` (prevents race conditions).

---

## Real-World References in Repository
- `feature/note/impl/.../presentation/state/NoteListState.kt`
- `feature/note/impl/.../presentation/intent/NoteListIntent.kt`
- `feature/note/impl/.../presentation/sideeffect/NoteListSideEffect.kt`
- `feature/media-orchestrator/impl/.../presentation/viewmodel/MediaOrchestratorViewModel.kt`
