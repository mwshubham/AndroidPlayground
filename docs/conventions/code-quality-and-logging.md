# Code Quality, Logging, and Architecture Rules

This document serves as the canonical source of truth for code quality, logging, static analysis, and architectural boundary rules in **AndroidPlayground**.

---

## 1. Logging Rules

**Rule**: `android.util.Log` is strictly forbidden across the entire project. Always use `timber.log.Timber`.

| Forbidden | Required replacement |
|---|---|
| `Log.d(TAG, msg)` | `Timber.d(msg)` |
| `Log.e(TAG, msg)` | `Timber.e(msg)` |
| `Log.i(TAG, msg)` | `Timber.i(msg)` |
| `Log.w(TAG, msg)` | `Timber.w(msg)` |
| `Log.v(TAG, msg)` | `Timber.v(msg)` |
| `Log.wtf(TAG, msg)` | `Timber.wtf(msg)` |

### Additional Logging Guardrails
- **No `import android.util.Log`**: Flag as an error even if the import is unused.
- **No manual `TAG` constant**: Timber automatically generates tags using the calling class name. Defining `private const val TAG = "..."` for logging is forbidden.
- **Dependency**: Add `implementation(libs.timber)` in the subproject's `build.gradle.kts` if logging is required in a module.
- **Enforcement**: This rule is enforced by Detekt (`ForbiddenImport`, `ForbiddenMethodCall`) with type resolution and will fail CI if violated.

---

## 2. Clean Architecture Boundary Rules

Every feature implementation must respect strict layer boundaries:
`presentation` → `domain` ← `data`

Flag any of the following as **errors**:

1. **Domain model exposed in presentation**:
   - A class from `domain/model/` imported directly into a `presentation/` composable or UI state.
   - **Fix**: Create a `{Entity}UiModel` in `presentation/model/` and map using a `{Entity}UiMapper` in `presentation/mapper/`.
2. **Impl-to-impl dependency**:
   - `implementation(project(":feature:X:impl"))` inside another feature's `build.gradle.kts`.
   - **Fix**: Depend strictly on `:feature:X:api`. Inter-feature communication must use Navigation routes (`NavKey`) or public API contracts.
3. **Use case bypassed**:
   - A `presentation/` file directly importing a `data/` class (such as a repository implementation or DAO).
   - **Fix**: Inject and call a domain `UseCase` (or repository interface) from the ViewModel.
4. **Transient side effect in State**:
   - Single-shot events (e.g., `showSnackbar: Boolean`, `navigateToDetail: Long?`) placed in `{Feature}State`.
   - **Fix**: Move transient events to `{Feature}SideEffect` emitted via a buffered `Channel`.

---

## 3. Data Layer Conventions

1. **Repository**:
   - Interface in `domain/repository/{Name}Repository.kt` (plain interface, no `Impl` suffix).
   - Implementation in `data/repository/{Name}RepositoryImpl.kt`.
   - Bound in DI module via Hilt `@Binds`.
2. **Room Database (when applicable)**:
   - Entity: `data/local/{Name}Entity.kt` annotated with `@Entity`.
   - DAO: `data/local/{Name}Dao.kt` annotated with `@Dao` (interface only).
   - Database: `data/local/{Name}Database.kt` annotated with `@Database` (abstract class).
   - Mapper: `data/local/{Name}Mapper.kt` mapping between Entity and Domain model.

---

## 4. Static Analysis and Formatting Commands

Before committing any code or finalizing a task, run:

```bash
./gradlew ktlintCheck detektCheckAll 2>&1 | grep -E "\.kt:[0-9]+|FAILED|BUILD SUCCESS"
```

### Dealing with Violations:
- **Ktlint formatting errors**: Run `./gradlew ktlintFormat` to automatically resolve formatting issues, then re-run `ktlintCheck`.
- **Detekt violations**: Fix the underlying violations in code. **Never** add baseline suppressions (`detekt-baseline.xml`) or `@Suppress` annotations unless explicitly instructed by the user.
- **Detekt Type Resolution**: `detektCheckAll` is configured with `debugCompileClasspath` across all subprojects, ensuring type-resolved rules like `ForbiddenMethodCall` catch all forbidden API usages.
