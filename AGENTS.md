# AI Agents & Automation Guide: AndroidPlayground

Welcome! This repository is configured for autonomous and semi-autonomous AI agents (including **Android Studio / Antigravity**, **GitHub Copilot**, **Claude Code**, **Cursor**, and **CI PR Bots**).

To maintain architectural integrity across this 45+ module project, all agents must adhere to the single source of truth and conventions documented below.

---

## 1. Single Source of Truth (SSOT) Architecture

All canonical architectural rules, templates, and conventions live in [`docs/conventions/`](file:///docs/conventions/):

| Area | Canonical Specification File | Scope |
|---|---|---|
| **MVI Pattern** | [`docs/conventions/mvi-pattern.md`](file:///docs/conventions/mvi-pattern.md) | State, Intent, SideEffect, ViewModel (3 separate files, no combined contract) |
| **Screens** | [`docs/conventions/compose-screens.md`](file:///docs/conventions/compose-screens.md) | Mandatory Screen/Content split, `TrackScreenViewEvent`, no UI in Screen |
| **UI Components** | [`docs/conventions/ui-components.md`](file:///docs/conventions/ui-components.md) | One component per file, custom previews (`@ComponentPreview`, `@DualThemePreview`), `PreviewContainer` |
| **Feature Structure** | [`docs/conventions/feature-module-structure.md`](file:///docs/conventions/feature-module-structure.md) | `:feature:{name}:api` and `:feature:{name}:impl` separation, Navigation 3 `NavKey` |
| **Quality & Logging** | [`docs/conventions/code-quality-and-logging.md`](file:///docs/conventions/code-quality-and-logging.md) | Timber only (`android.util.Log` forbidden), Clean Architecture boundaries, Detekt |

For broad architecture overviews, design patterns, and module graphs, see the [Documentation Hub (`docs/`)](docs/README.md).

---

## 2. Available Agents & Skills

Agents can invoke or follow these specialized workflows:

### A. Code Review
- **Skill**: [`.github/skills/android-code-review/SKILL.md`](file:///.github/skills/android-code-review/SKILL.md) (universal Agent Skills format)
- **Copilot Agent**: [`.github/agents/android-code-review.agent.md`](file:///.github/agents/android-code-review.agent.md)
- **Function**: Validates changed files against MVI rules, Screen/Content separation, preview annotations, clean architecture boundaries, and runs Detekt / ktlint.

### B. Android Platform Skills
Located in [`.github/skills/`](file:///.github/skills/):
- **`edge-to-edge`**: Migrate Compose screens to edge-to-edge with safe drawing and IME insets.
- **`navigation-3`**: Implement type-safe Navigation 3 routes, scene transitions, and backstacks.
- **`r8-analyzer`**: Audit ProGuard and R8 keep rules.
- **`agp-9-upgrade`**: Guide AGP 9 migrations.
- **`migrate-xml-views-to-jetpack-compose`**: Convert XML layouts to Compose.

---

## 3. Essential Verification Commands

Any agent modifying Kotlin or Gradle files **must** execute this command before considering any task complete:

```bash
./gradlew ktlintCheck detektCheckAll 2>&1 | grep -E "\.kt:[0-9]+|FAILED|BUILD SUCCESS"
```

- **Formatting violations**: Run `./gradlew ktlintFormat` to automatically format files.
- **Detekt violations**: Fix in code. Never add baseline suppressions (`detekt-baseline.xml`) or `@Suppress` annotations without explicit user approval.
- **Unit tests**: Run `./gradlew test` or `./gradlew :feature:{name}:impl:test` to verify behavior.

---

## 4. Key Architectural Invariants

1. **No Combined Contracts**: `{Feature}State.kt`, `{Feature}Intent.kt`, and `{Feature}SideEffect.kt` must exist in separate files. Never create `{Feature}Contract.kt`.
2. **Screen vs Content**: `{Feature}Screen.kt` (public) wires the ViewModel and collects side-effects; `{Feature}Content.kt` (internal) renders pure UI.
3. **No Direct Log Calls**: Always use `timber.log.Timber`. Never import `android.util.Log` or declare manual `TAG` constants.
4. **Clean Architecture Boundaries**: Presentation models must be `{Entity}UiModel` mapped from domain models. Feature `impl` modules must never depend on another feature's `impl` module.
