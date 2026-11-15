# AndroidPlayground Documentation Hub

Welcome to the central documentation index for **AndroidPlayground**. This directory is the **authoritative Single Source of Truth (SSOT)** for project architecture, coding standards, design patterns, and engineering roadmaps.

---

## 📚 Documentation Index

### 1. Canonical Coding & Architectural Conventions (`docs/conventions/`)
The non-negotiable rules for all engineers and AI assistants working in this repository:
- [**MVI Pattern Architecture**](conventions/mvi-pattern.md): Strict 3-file rule (`State`, `Intent`, `SideEffect`), ViewModel invariants, and forbidden combined contracts.
- [**Compose Screen / Content Split**](conventions/compose-screens.md): Mandatory 2-file separation (`*Screen.kt` for ViewModel wiring, `*Content.kt` for pure UI).
- [**UI Components & Previews**](conventions/ui-components.md): One component per file, `@ComponentPreview`, `@DualThemePreview`, and `PreviewContainer` rules.
- [**Feature Module Structure**](conventions/feature-module-structure.md): Partitioning into `:feature:{name}:api` (Navigation 3 `NavKey` routes) and `:feature:{name}:impl`.
- [**Code Quality, Logging & Clean Architecture**](conventions/code-quality-and-logging.md): Strict Timber logging (`android.util.Log` forbidden), Clean Architecture boundaries, Detekt, and ktlint.

### 2. Architecture & Modularization
- [**Module Structure & System Design Guide**](MODULE_STRUCTURE.md): Detailed explanation of Clean Architecture in the project, layer dependencies (`presentation` → `domain` ← `data`), and adding new feature modules.
- [**Module Dependency Graph**](module-graph.md): Complete interactive Mermaid diagram of all 45+ subprojects and their inter-module dependencies (auto-verified and updated by CI).

### 3. Software Design & Principles
- [**System Design & Design Patterns**](DESIGN_PATTERNS.md): Comprehensive guide to design patterns implemented in the project (Creational, Structural, Behavioral) with real-world Android scenarios.
- [**SOLID Principles Guide**](SOLID_PRINCIPLES.md): In-depth guide with Kotlin code examples illustrating SOLID principles and clean architecture practices.

### 4. Engineering Backlog & Exploration
- [**TODO & Research Topics**](TODO.md): Backlog of upcoming engineering investigations, network latency optimizations (HTTP/2, TLS), performance benchmarking, OpenTelemetry, and profiling tasks.

### 5. AI Tooling & Automation
- [**AI Agents Guide (AGENTS.md)**](../AGENTS.md): Universal guide for AI assistants (Antigravity, Copilot, Claude Code, Cursor) describing how agents interface with `docs/conventions/` and executable skills in `.github/skills/`.
