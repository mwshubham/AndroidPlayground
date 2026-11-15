---
description: "Use when implementing MVI (Model-View-Intent) pattern, creating State, Intent, SideEffect, or ViewModel classes for any feature screen. Covers file locations, naming, class shapes, and ViewModel boilerplate."
applyTo: "**/presentation/**/*.kt"
---

# MVI Pattern (Copilot Trigger)

> **Canonical Source of Truth**: [docs/conventions/mvi-pattern.md](../../docs/conventions/mvi-pattern.md)

## Core Invariants
- **Three Separate Files**: MVI contracts are **always** split into three separate files. A single combined `*Contract.kt` is forbidden.
  - `{Feature}State.kt` in `presentation/state/` (data class, all fields with default values)
  - `{Feature}Intent.kt` in `presentation/intent/` (sealed interface, user actions)
  - `{Feature}SideEffect.kt` in `presentation/sideeffect/` (sealed interface, one-time events)
  - `{Feature}ViewModel.kt` in `presentation/viewmodel/` (@HiltViewModel)
- **ViewModel**: Uses `MutableStateFlow` (as `StateFlow`), `Channel.BUFFERED` for side-effects, single `handleIntent()`, and `_state.update { }`.

Refer to the canonical specification at `docs/conventions/mvi-pattern.md` for full templates and real-world examples.
