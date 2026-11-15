---
description: "Use when creating or editing a UI component composable — any reusable Composable that is not a Screen. Covers one-component-per-file rule, mandatory @Preview requirements, custom preview annotations, and the PreviewContainer wrapper."
applyTo: "**/presentation/component/**/*.kt"
---

# UI Components & Previews (Copilot Trigger)

> **Canonical Source of Truth**: [docs/conventions/ui-components.md](../../docs/conventions/ui-components.md)

## Core Invariants
- **One Component per File**: Each UI component lives in its own file under `presentation/component/`.
- **Custom Previews**: Raw `@Preview` is forbidden. Use `@ComponentPreview`, `@DualThemePreview`, `@PhonePreview`, or `@FullPreview` from `core.ui.preview`.
- **PreviewContainer**: Always wrap preview content in `PreviewContainer { ... }`.
- **Preview Function**: Must be `private` and named `{ComponentName}Preview`.
- **Parameter Order**: `modifier: Modifier = Modifier` must be the last parameter before trailing lambdas.

Refer to the canonical specification at `docs/conventions/ui-components.md` for full templates and usage examples.
