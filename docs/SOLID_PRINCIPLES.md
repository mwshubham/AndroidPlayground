# SOLID Principles in AndroidPlayground

A code-heavy reference showing where and how each SOLID principle is applied across this project.
Every example links to a real file in the codebase so you can read the full context.

---

## Table of Contents

1. [S — Single Responsibility Principle](#1-s--single-responsibility-principle)
2. [O — Open/Closed Principle](#2-o--openclosed-principle)
3. [L — Liskov Substitution Principle](#3-l--liskov-substitution-principle)
4. [I — Interface Segregation Principle](#4-i--interface-segregation-principle)
5. [D — Dependency Inversion Principle](#5-d--dependency-inversion-principle)

---

## 1. S — Single Responsibility Principle

> **A class (or file) should have only one reason to change.**

### 1.1 — One Use Case per Class

Each use case in the domain layer does exactly one thing. If the business rule for that operation changes, only that file changes.

**Reference:** [feature/note/impl — domain/usecase/](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/usecase/)

```kotlin
// GetNotesUseCase.kt — only responsibility: stream all notes from the repository
class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}
```

```kotlin
// InsertNoteUseCase.kt — only responsibility: create a new note
class InsertNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
) {
    suspend operator fun invoke(title: String, content: String): Long =
        repository.insertNote(
            Note(
                title = title,
                content = content,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
            ),
        )
}
```

The note module has five separate use case files (`GetNotesUseCase`, `GetNoteByIdUseCase`, `InsertNoteUseCase`, `UpdateNoteContentUseCase`, `DeleteNoteUseCase`). A change to the delete logic cannot accidentally affect the insert logic because they live in completely separate classes.

---

### 1.2 — Separate Mappers per Layer Boundary

Two different mapper files translate between the three layers. Each has exactly one reason to change: the shape of the data on its respective side of the boundary.

| File | Responsibility | Converts |
|------|---------------|----------|
| [`NoteMapper.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/local/NoteMapper.kt) | Persistence ↔ Domain | `NoteEntity` ↔ `Note` |
| [`NoteUiMapper.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/mapper/NoteUiMapper.kt) | Domain → UI | `Note` → `NoteDetailUiModel` / `NoteListItemUiModel` |

```kotlin
// NoteMapper.kt — data layer: bridges Room entity to domain model
fun NoteEntity.toDomainModel(): Note = Note(
    id = id, title = title, content = content,
    createdAt = createdAt, updatedAt = updatedAt,
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id, title = title, content = content,
    createdAt = createdAt, updatedAt = updatedAt,
)
```

```kotlin
// NoteUiMapper.kt — presentation layer: pre-formats strings for the UI
object NoteUiMapper {
    fun toUiModel(note: Note): NoteDetailUiModel =
        NoteDetailUiModel(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAtFormatted = DateFormatter.formatTimestamp(note.createdAt),
            updatedAtFormatted = DateFormatter.formatTimestamp(note.updatedAt),
        )

    fun toListUiModel(note: Note): NoteListItemUiModel =
        NoteListItemUiModel(
            id = note.id,
            title = note.title,
            content = note.content,
            updatedAtFormatted = DateFormatter.formatTimestamp(note.updatedAt),
        )
}
```

If the database schema changes, only `NoteMapper.kt` changes.
If the UI needs a new formatted field, only `NoteUiMapper.kt` changes.

---

### 1.3 — MVI Contracts in Separate Files

MVI contracts are never combined into a single `*Contract.kt` file. Each concern has its own file and directory so that a change to what the screen shows (`State`) does not touch how users interact with it (`Intent`) or what side effects it emits (`SideEffect`).

**Reference:** [feature/note/impl — presentation/](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/)

```
presentation/
├── state/
│   └── NoteListState.kt        ← only responsibility: what the screen displays
├── intent/
│   └── NoteListIntent.kt       ← only responsibility: what the user can do
└── sideeffect/
    └── NoteListSideEffect.kt   ← only responsibility: one-time events (navigate, toast)
```

```kotlin
// NoteListState.kt — owns the display data, nothing else
data class NoteListState(
    val notes: List<NoteListItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
)
```

```kotlin
// NoteListIntent.kt — owns the user action vocabulary, nothing else
sealed interface NoteListIntent {
    data object LoadNotes : NoteListIntent
    data class SearchNotes(val query: String) : NoteListIntent
    data class DeleteNote(val noteId: Long) : NoteListIntent
    data class NavigateToDetail(val noteId: Long) : NoteListIntent
    data object NavigateToAdd : NoteListIntent
    data object NavigateBack : NoteListIntent
}
```

```kotlin
// NoteListSideEffect.kt — owns one-time events, nothing else
sealed interface NoteListSideEffect {
    data class NavigateToNoteDetail(val noteId: Long) : NoteListSideEffect
    data object NavigateToAddNote : NoteListSideEffect
    data object NavigateBack : NoteListSideEffect
    data class ShowSuccessMessage(val message: String) : NoteListSideEffect
    data class ShowErrorMessage(val message: String) : NoteListSideEffect
}
```

---

### 1.4 — Screen / Content Split

Every screen is split across two files. This ensures lifecycle and layout concerns never end up in the same class.

**Reference:** [feature/note/impl — presentation/screen/NoteListScreen.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/screen/NoteListScreen.kt) and [presentation/component/NoteListContent.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/component/NoteListContent.kt)

| File | Single Responsibility |
|------|-----------------------|
| `NoteListScreen.kt` | Wires `hiltViewModel()`, collects `StateFlow`, handles `SideEffect` in `LaunchedEffect`, delegates all layout to `NoteListContent` |
| `NoteListContent.kt` | Pure Compose layout; accepts `state` + `onIntent` lambda; has its own `@Preview`; no ViewModel, no lifecycle |

```kotlin
// NoteListScreen.kt — only responsible for lifecycle wiring
@Composable
fun NoteListScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is NoteListSideEffect.NavigateToNoteDetail -> onNavigateToDetail(sideEffect.noteId)
                is NoteListSideEffect.NavigateToAddNote    -> onNavigateToAdd()
                is NoteListSideEffect.NavigateBack         -> onNavigateBack()
                is NoteListSideEffect.ShowSuccessMessage   -> snackbarHostState.showSnackbar(sideEffect.message)
                is NoteListSideEffect.ShowErrorMessage     -> snackbarHostState.showSnackbar(sideEffect.message)
            }
        }
    }

    NoteListContent(          // ← delegates all UI to the Content composable
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
```

---

## 2. O — Open/Closed Principle

> **Software entities should be open for extension but closed for modification.**

### 2.1 — Sealed Classes as an Extensible Vocabulary

New topics, intents, and side effects are added by extending the sealed class/interface hierarchy — never by modifying existing branches.

**Reference:** [feature/feed/impl — domain/model/TopicId.kt](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/domain/model/TopicId.kt)

```kotlin
// TopicId.kt — add a new feature topic by adding a new data object; existing ones are untouched
sealed class TopicId {
    data object ImageUploadApp         : TopicId()
    data object LoginScreen            : TopicId()
    data object NoteApp                : TopicId()
    data object MediaOrchestratorApp   : TopicId()
    data object UserInitiatedServiceApp: TopicId()
    data object AndroidSecurity        : TopicId()
    data object RoomDatabaseApp        : TopicId()
    data object InterAppCommunication  : TopicId()
    data object GraphQL                : TopicId()
    data object Media3Player           : TopicId()
    data object WebSocket              : TopicId()
    data object Sse                    : TopicId()
    data object Grpc                   : TopicId()
    data object TicTacToe              : TopicId()
    // Adding a new feature? Append here. Nothing else needs to change in this file.
}
```

The same pattern applies to `NoteListIntent` and `NoteListSideEffect`. A new action or event is a new `data class`/`data object` in the sealed interface. The exhaustive `when` blocks in the ViewModel and Screen catch omissions at compile time, so you are guided to handle the new case rather than silently ignoring it.

---

### 2.2 — Navigation Multibinding: Features Register Themselves

The core navigation composable (`AppNavigation`) is **closed**: it never needs to be modified when a new feature is added. Features are **open for extension**: each feature's DI module adds its screen to the navigation graph via Hilt's `@IntoSet` multibinding.

**Reference:** [feature/feed/impl — di/FeedNavigationModule.kt](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/di/FeedNavigationModule.kt) and [core/navigation — AppNavigation.kt](../core/navigation/src/main/kotlin/com/example/android/playground/core/navigation/AppNavigation.kt)

```kotlin
// AppNavigation.kt — never modified when features are added or removed
@Composable
fun AppNavigation(
    navigator: AppNavigator,
    backStack: List<NavKey>,
    entryProviderScopes: Set<EntryProviderInstaller>,   // ← Set injected by Hilt
) {
    NavDisplay(
        backStack = backStack,
        onBack = { navigator.goBack() },
        entryProvider = entryProvider {
            entryProviderScopes.forEach { builder -> this.builder() }  // ← each feature adds itself
        },
        // ...
    )
}
```

```kotlin
// FeedNavigationModule.kt — feed feature registers itself without touching AppNavigation
@Module
@InstallIn(ActivityRetainedComponent::class)
object FeedNavigationModule {

    private val topicRoutes: Map<TopicId, NavKey> = mapOf(
        TopicId.NoteApp              to NoteListRoute,
        TopicId.ImageUploadApp       to ImageUploadRoute,
        TopicId.MediaOrchestratorApp to MediaOrchestratorRoute,
        // ... all 14 topics
    )

    @IntoSet          // ← contributes to Set<EntryProviderInstaller>
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller = {
        entry<FeedRoute> {
            FeedScreen(
                onTopicClick = { topicId ->
                    topicRoutes[topicId]?.let { navigator.goTo(it) }
                },
            )
        }
    }
}
```

Adding a new feature module means creating a new `*NavigationModule.kt` with `@IntoSet`. `AppNavigation.kt` stays untouched.

---

### 2.3 — Feature API / Impl Split

The public API surface of a feature (routes, shared models) is a separate Gradle module (`feature/{name}/api`). The implementation details live in `feature/{name}/impl`. Other features depend only on the `api` module; the `impl` can be extended freely without breaking consumers.

```
feature/note/
├── api/    ← closed to callers: NoteListRoute, NoteDetailRoute
└── impl/   ← open for extension: add use cases, screens, data sources freely
```

---

## 3. L — Liskov Substitution Principle

> **Objects of a subtype must be substitutable for objects of their supertype without altering the correctness of the program.**

### 3.1 — Repository Implementations Fully Honor Their Interfaces

`NoteRepositoryImpl` can be used everywhere `NoteRepository` is expected without any change in behavior.

**Reference:** [domain/repository/NoteRepository.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/repository/NoteRepository.kt) and [data/repository/NoteRepositoryImpl.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/repository/NoteRepositoryImpl.kt)

```kotlin
// NoteRepository.kt — the contract
interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>          // reactive stream
    suspend fun getNoteById(id: Long): Note?    // nullable: returns null, never throws, when not found
    suspend fun insertNote(note: Note): Long    // returns generated ID
    suspend fun updateNoteContent(id: Long, title: String, content: String)
    suspend fun deleteNoteById(id: Long)
    suspend fun getNoteCount(): Int
}
```

```kotlin
// NoteRepositoryImpl.kt — the substitutable concrete type
@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes().map { it.toDomainModelList() }

    // Honors the nullable contract: returns null instead of throwing NoSuchElementException
    override suspend fun getNoteById(id: Long): Note? =
        noteDao.getNoteById(id)?.toDomainModel()

    override suspend fun insertNote(note: Note): Long =
        noteDao.insertNote(note.toEntity())

    override suspend fun updateNoteContent(id: Long, title: String, content: String) {
        noteDao.updateNoteContent(
            id = id, title = title, content = content,
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun deleteNoteById(id: Long) = noteDao.deleteNoteById(id)

    override suspend fun getNoteCount(): Int = noteDao.getNoteCount()
}
```

LSP compliance checklist for this pair:

| Contract requirement | Implementation |
|---------------------|----------------|
| `getAllNotes()` returns a reactive `Flow` | ✅ returns `Flow` from Room DAO |
| `getNoteById()` returns `Note?` (null-safe) | ✅ returns `null` (not an exception) if not found |
| `insertNote()` returns the generated row ID | ✅ delegates to Room's `insert` which returns `Long` |
| All `suspend` methods preserve async semantics | ✅ no blocking calls on main thread |

The same pattern holds for every other repository pair in the project:
`FeedRepositoryImpl : FeedRepository`, `ImageUploadRepositoryImpl : ImageUploadRepository`.

---

## 4. I — Interface Segregation Principle

> **Clients should not be forced to depend on methods they do not use.**

### 4.1 — Repository Interfaces Are Narrow by Feature

Each repository exposes only the operations relevant to its feature. No feature is forced to depend on a bloated "super-repository" with unrelated methods.

**Reference:** [feed/domain/repository/FeedRepository.kt](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/domain/repository/FeedRepository.kt) and [imageupload/domain/repository/](../feature/image-upload/impl/src/main/kotlin/com/example/android/playground/imageupload/domain/repository/)

```kotlin
// FeedRepository.kt — one method; the feed feature needs only topic loading
interface FeedRepository {
    suspend fun getTopics(): List<Topic>
}
```

```kotlin
// ImageUploadRepository.kt — one method; upload logic is the only concern
interface ImageUploadRepository {
    suspend fun uploadImage(imageId: String): ImageUploadResult
}
```

```kotlin
// ImageUploadStateRepository.kt — three tightly-related state methods; not mixed with upload I/O
interface ImageUploadStateRepository {
    val state: StateFlow<ImageUploadState>
    fun updateState(newState: ImageUploadState)
    fun clearState()
    fun getApplicationScope(): CoroutineScope
}
```

The upload state concerns (`updateState`, `clearState`) are segregated into their own interface instead of being bolted onto `ImageUploadRepository`. A ViewModel that only reads state depends on `ImageUploadStateRepository`; a background worker that only uploads depends on `ImageUploadRepository`. Neither is forced to depend on the other's methods.

---

### 4.2 — Use Cases Are the Finest-Grained Interface

Use cases act as single-method functional interfaces. A ViewModel injects only the use cases it actually calls — it is never forced to depend on the entire repository with its six operations.

**Reference:** [NoteListViewModel.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/viewmodel/NoteListViewModel.kt)

```kotlin
// NoteListViewModel.kt — depends on exactly the two operations it needs
@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,    // ← only needs to load
    private val deleteNoteUseCase: DeleteNoteUseCase, // ← only needs to delete
) : ViewModel() { /* ... */ }
```

`NoteListViewModel` never sees `InsertNoteUseCase`, `UpdateNoteContentUseCase`, or `GetNoteByIdUseCase`. Compare this to depending directly on `NoteRepository`, which would expose all six methods regardless of whether the ViewModel uses them.

---

## 5. D — Dependency Inversion Principle

> **High-level modules should not depend on low-level modules. Both should depend on abstractions.**

### 5.1 — Hilt `@Binds` Wires Abstractions to Implementations

`NoteModule` is the only place in the entire codebase where `NoteRepositoryImpl` is mentioned. Everything above it (use cases, ViewModel) depends on the `NoteRepository` abstraction.

**Reference:** [feature/note/impl — di/NoteModule.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/di/NoteModule.kt)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {

    // Binds the abstraction to the concrete class — callers never import NoteRepositoryImpl
    @Binds
    fun bindNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository

    companion object {
        @Provides
        @Singleton
        fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
            Room.databaseBuilder(
                context = context.applicationContext,
                klass = NoteDatabase::class.java,
                name = NoteDatabase.DATABASE_NAME,
            ).build()

        @Provides
        fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
    }
}
```

Dependency graph:

```
NoteListViewModel
    └── GetNotesUseCase         (depends on NoteRepository ← abstraction)
    └── DeleteNoteUseCase       (depends on NoteRepository ← abstraction)
            └── NoteRepository  (interface — the abstraction)
                    └── NoteRepositoryImpl  (concrete — wired by NoteModule @Binds)
                            └── NoteDao     (Room — low-level detail)
```

Swapping the implementation (e.g., from Room to an in-memory fake for tests) requires changing only the `@Binds` binding in `NoteModule` — no other file changes.

---

### 5.2 — ViewModels Depend on Use Cases, Not Repositories

`NoteListViewModel` declares its dependencies as use case types, not as repository types. This means the ViewModel is decoupled from any data-layer detail.

```kotlin
// High-level module: depends on use case abstractions, not NoteRepositoryImpl
@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
) : ViewModel() {

    val state: StateFlow<NoteListState> = /* combines getNotesUseCase() + searchQuery */

    fun handleIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.DeleteNote -> viewModelScope.launch {
                deleteNoteUseCase(intent.noteId)
            }
            // ...
        }
    }
}
```

---

### 5.3 — `AppNavigator` Abstracts Navigation Internals

Feature screens call `navigator.goTo(route)` and `navigator.goBack()`. They depend on this simple abstraction, not on Navigation 3's internal `SnapshotStateList<NavKey>` or any other implementation detail.

**Reference:** [core/navigation — AppNavigator.kt](../core/navigation/src/main/kotlin/com/example/android/playground/core/navigation/AppNavigator.kt)

```kotlin
// AppNavigator.kt — the abstraction features depend on
@ActivityRetainedScoped
class AppNavigator(startDestination: NavKey) {
    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startDestination)

    fun goTo(destination: NavKey) { backStack.add(destination) }
    fun goBack() { backStack.removeLastOrNull() }
}
```

Feature DI modules receive `AppNavigator` via constructor injection and call only `goTo` and `goBack`. They know nothing about how the back stack is stored or how transitions are animated.

---

### 5.4 — Feature Modules Never Depend on Each Other

The module dependency graph enforces DIP at the build level. Feature impl modules are only allowed to depend on:

- Their own `feature/{name}/api` module
- `core:navigation`, `core:ui`, `core:common`, `core:analytics` (all abstractions)

They are **never** allowed to depend on another feature's `impl` module. This means no high-level feature (`note`) can ever reach into the internals of another feature (`feed`). All cross-feature coupling goes through the `api` layer or through `core` modules.

```
feature/note/impl
    ├── :feature:note:api         ← own public contract
    ├── :core:navigation          ← AppNavigator abstraction
    ├── :core:ui                  ← shared Compose theme & components
    └── :core:common              ← shared utilities
    ✗   :feature:feed:impl        ← FORBIDDEN: never depends on another feature's impl
```

---

## Quick Reference

| Principle | Where applied | Key files |
|-----------|--------------|-----------|
| **S** — Single Responsibility | One use case per class | [`GetNotesUseCase.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/usecase/GetNotesUseCase.kt), [`InsertNoteUseCase.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/usecase/InsertNoteUseCase.kt) |
| **S** — Single Responsibility | Separate mappers per layer | [`NoteMapper.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/local/NoteMapper.kt), [`NoteUiMapper.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/mapper/NoteUiMapper.kt) |
| **S** — Single Responsibility | MVI in separate files | [`NoteListState.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/state/NoteListState.kt), [`NoteListIntent.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/intent/NoteListIntent.kt), [`NoteListSideEffect.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/sideeffect/NoteListSideEffect.kt) |
| **S** — Single Responsibility | Screen / Content split | [`NoteListScreen.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/screen/NoteListScreen.kt), [`NoteListContent.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/component/NoteListContent.kt) |
| **O** — Open/Closed | Sealed class hierarchy | [`TopicId.kt`](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/domain/model/TopicId.kt), `NoteListIntent.kt`, `NoteListSideEffect.kt` |
| **O** — Open/Closed | Navigation multibinding | [`FeedNavigationModule.kt`](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/di/FeedNavigationModule.kt), [`AppNavigation.kt`](../core/navigation/src/main/kotlin/com/example/android/playground/core/navigation/AppNavigation.kt) |
| **O** — Open/Closed | API / Impl module split | `feature/note/api`, `feature/note/impl` |
| **L** — Liskov Substitution | Repository impl honors interface | [`NoteRepository.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/repository/NoteRepository.kt), [`NoteRepositoryImpl.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/repository/NoteRepositoryImpl.kt) |
| **I** — Interface Segregation | Narrow repository interfaces | [`FeedRepository.kt`](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/domain/repository/FeedRepository.kt), [`ImageUploadRepository.kt`](../feature/image-upload/impl/src/main/kotlin/com/example/android/playground/imageupload/domain/repository/ImageUploadRepository.kt) |
| **I** — Interface Segregation | ViewModel injects only needed use cases | [`NoteListViewModel.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/viewmodel/NoteListViewModel.kt) |
| **D** — Dependency Inversion | Hilt `@Binds` abstracts concrete types | [`NoteModule.kt`](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/di/NoteModule.kt) |
| **D** — Dependency Inversion | Navigator abstraction over Nav3 internals | [`AppNavigator.kt`](../core/navigation/src/main/kotlin/com/example/android/playground/core/navigation/AppNavigator.kt) |
| **D** — Dependency Inversion | Module graph forbids impl→impl deps | `settings.gradle.kts`, each module's `build.gradle.kts` |
