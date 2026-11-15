# UI Components & Previews

This document serves as the canonical source of truth for creating and organizing reusable UI components in **AndroidPlayground**.

---

## Rule: One Component Per File, Every File Has a Preview

Every UI component lives in its **own file** under `presentation/component/`. A file must never contain more than one public composable. Every component file **must** include at least one `@Preview` composable.

---

## 1. Component Template

```kotlin
package com.example.android.playground.{feature}.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
fun {ComponentName}(
    // State properties passed as plain parameters
    modifier: Modifier = Modifier,
    // Trailing lambdas/callbacks
) {
    // Composable layout
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun {ComponentName}Preview() {
    PreviewContainer {
        {ComponentName}(
            // Sample preview data
        )
    }
}
```

---

## 2. Preview Annotations — Always Use Custom Annotations

**Do not** use raw `@Preview` from `androidx.compose.ui.tooling.preview`. Always use the custom annotations from `com.example.android.playground.core.ui.preview`:

```kotlin
import com.example.android.playground.core.ui.preview.ComponentPreview   // Light + Dark, component size
import com.example.android.playground.core.ui.preview.DualThemePreview   // Light + Dark (no device frame)
import com.example.android.playground.core.ui.preview.PhonePreview       // Pixel 4 Light + Dark
import com.example.android.playground.core.ui.preview.FullPreview        // All of the above combined
import com.example.android.playground.core.ui.preview.PreviewContainer   // Theme and surface wrapper
```

| Annotation | Use Case |
|---|---|
| `@ComponentPreview` | **Default** for most UI components (cards, chips, buttons, list rows). |
| `@DualThemePreview` | For larger components without fixed device frames. |
| `@PhonePreview` | For layouts that are sensitive to screen dimensions and insets. |
| `@FullPreview` | Full-screen components or complex layouts. |

---

## 3. Rules & Invariants

1. **One Public Composable per File**: Break smaller sub-components into private functions or separate files.
2. **Pure Data Parameters**: Pass immutable models or primitives. Never pass ViewModels, `Flow`, or `StateFlow`.
3. **Parameter Ordering**: `modifier: Modifier = Modifier` must be the last parameter before trailing lambdas (or the final parameter if no lambdas).
4. **Theme Wrapper**: All previews must wrap their composable content in `PreviewContainer { ... }`.
5. **Preview Function Visibility**: Preview functions must always be `private` and named `{ComponentName}Preview` (or `{ComponentName}{State}Preview` for multi-state variants).
