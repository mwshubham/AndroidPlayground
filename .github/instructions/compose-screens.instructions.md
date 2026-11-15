---
description: "Use when creating or editing a Compose screen — any file named *Screen.kt or *Content.kt in a presentation layer. Covers the mandatory Screen/Content split pattern, ViewModel wiring, side-effect collection, and preview requirements."
applyTo: "**/presentation/screen/**/*.kt"
---

# Compose Screen / Content Pattern (Copilot Trigger)

> **Canonical Source of Truth**: [docs/conventions/compose-screens.md](../../docs/conventions/compose-screens.md)

## Core Invariants
- **Always Split a Screen into Two Files**:
  - `{Feature}Screen.kt` in `presentation/screen/`: `public`, ViewModel wiring, state/side-effect collection, `TrackScreenViewEvent`. Contains **no** `Scaffold` or layout composables.
  - `{Feature}Content.kt` in `presentation/component/`: `internal`, pure Compose UI taking `(state, onIntent, modifier)`. **No** ViewModel calls. Includes at least one preview.

Refer to the canonical specification at `docs/conventions/compose-screens.md` for full templates and checklists.
