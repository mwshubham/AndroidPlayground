---
description: "Use when creating a new feature module, adding a new screen to an existing feature, or scaffolding the full directory structure for a feature. Covers api/impl split, required files per layer, build.gradle.kts setup, and settings.gradle.kts registration."
---

# Feature Module Structure (Copilot Trigger)

> **Canonical Source of Truth**: [docs/conventions/feature-module-structure.md](../../docs/conventions/feature-module-structure.md) and [docs/MODULE_STRUCTURE.md](../../docs/MODULE_STRUCTURE.md)

## Core Invariants
- **`api` and `impl` Split**:
  - `feature/{name}/api`: Contains navigation routes (`{Name}Routes.kt`) implementing `NavKey` and `@Serializable`. No UI, no ViewModels.
  - `feature/{name}/impl`: Full implementation across `presentation/`, `domain/`, `data/`, and `di/`.
- **Clean Architecture Boundaries**: `presentation` → `domain` ← `data`. Impl modules must never depend on another feature's impl module (only `:feature:*:api`).

Refer to the canonical specification at `docs/conventions/feature-module-structure.md` for build.gradle.kts templates and registration steps.
