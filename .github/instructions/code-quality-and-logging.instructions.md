---
description: "Use when checking or fixing code quality, formatting, logging, detekt static analysis, ktlint, or clean architecture boundaries across any Kotlin or Gradle file."
applyTo: "**/*.kt"
---

# Code Quality & Logging (Copilot Trigger)

> **Canonical Source of Truth**: [docs/conventions/code-quality-and-logging.md](../../docs/conventions/code-quality-and-logging.md)

## Core Invariants
- **Timber Logging Only**: `android.util.Log` is strictly forbidden. Always use `timber.log.Timber`. Flag any `import android.util.Log` or manual `TAG` constants.
- **Detekt & Ktlint**:
  Run `./gradlew ktlintCheck detektCheckAll 2>&1 | grep -E "\.kt:[0-9]+|FAILED|BUILD SUCCESS"`
  - Fix formatting with `./gradlew ktlintFormat`.
  - Fix detekt violations in code (never add baseline suppressions).
- **Clean Architecture Boundaries**:
  - Domain models from `domain/model/` must never be used in `presentation/` (map via `{Entity}UiModel` and `{Entity}UiMapper`).
  - Impl-to-impl dependencies are forbidden (depend on `api` only).

Refer to the canonical specification at `docs/conventions/code-quality-and-logging.md` for full rules.
