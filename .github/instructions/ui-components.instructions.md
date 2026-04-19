---
description: "Use when creating or editing a UI component composable — any reusable Composable that is not a Screen. Covers one-component-per-file rule, mandatory @Preview requirements, custom preview annotations, and the PreviewContainer wrapper."
applyTo: "**/presentation/component/**/*.kt"
---

# UI Components

## Rule: One Component Per File, Every File Has a Preview

Every UI component lives in its **own file** under `presentation/component/`. A file must never contain more than one public composable. Every component file **must** include at least one `@Preview` composable.

---

## Component Template

```kotlin
package com.example.android.playground.{feature}.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer

@Composable
fun {ComponentName}(
    // params — all state passed in, no ViewModel access
    modifier: Modifier = Modifier,
) {
    // composable body
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun {ComponentName}Preview() {
    PreviewContainer {
        {ComponentName}(
            // sample data
        )
    }
}
```

---

## Preview Annotations — Always Use Custom Annotations

**Do not** use raw `@Preview`. Use the custom annotations from `core.ui.preview`:

```kotlin
import com.example.android.playground.core.ui.preview.ComponentPreview   // light + dark
import com.example.android.playground.core.ui.preview.DualThemePreview   // light + dark (no device)
import com.example.android.playground.core.ui.preview.PhonePreview        // Pixel 4 light + dark
import com.example.android.playground.core.ui.preview.FullPreview         // all of the above
import com.example.android.playground.core.ui.preview.PreviewContainer    // theme wrapper
```

| Annotation | Use when |
|------------|----------|
| `@ComponentPreview` | Default for most components — generates light + dark previews |
| `@DualThemePreview` | When you also want theme-grouped previews without a device frame |
| `@PhonePreview` | When device constraints matter (e.g. width-dependent layouts) |
| `@FullPreview` | Full-page components / Content composables |

---

## Multi-State Previews

When a component has **distinct visual states**, add a named `@Preview` for each state:

```kotlin
@ComponentPreview
@Composable
private fun StatusChipActivePreview() {
    PreviewContainer { StatusChip(status = Status.Active) }
}

@ComponentPreview
@Composable
private fun StatusChipErrorPreview() {
    PreviewContainer { StatusChip(status = Status.Error) }
}

@ComponentPreview
@Composable
private fun StatusChipLoadingPreview() {
    PreviewContainer { StatusChip(status = Status.Loading) }
}
```

Named `@Preview(name = "...")` variants are also acceptable when you need fine-grained control:

```kotlin
@Preview(showBackground = true, name = "ConceptCard — Expanded")
@Composable
private fun ConceptCardExpandedPreview() { ... }

@Preview(showBackground = true, name = "ConceptCard — Collapsed")
@Composable
private fun ConceptCardCollapsedPreview() { ... }
```

---

## Rules Summary

- [ ] **One public composable per file** — no exceptions
- [ ] All composable parameters are **plain data** (no ViewModel, no `Flow`, no `State<T>`)
- [ ] `modifier: Modifier = Modifier` is always the **last** parameter before callbacks
- [ ] Every file has **at least one** `@Preview` — use `@ComponentPreview` by default
- [ ] Preview functions are always **`private`**
- [ ] Wrap preview content in `PreviewContainer { }` so it receives the app theme
- [ ] Add extra named previews for every distinct visual state the component can have
- [ ] Never call `hiltViewModel()` or `collectAsStateWithLifecycle()` inside a component

---

## Real-World Reference

Good examples to follow:
- `feature/user-initiated-service/impl/.../presentation/component/ConceptCard.kt` — collapsible card with two named previews
- `feature/media-orchestrator/impl/.../presentation/component/StatusChip.kt` — simple state-driven chip with `@ComponentPreview`
- `feature/media-orchestrator/impl/.../presentation/component/MediaItemCard.kt` — list item with data model parameter
