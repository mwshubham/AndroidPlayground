# Design Patterns in AndroidPlayground

A comprehensive reference of every design pattern used across this project.
Each entry names the pattern, explains what problem it solves here, and links to a concrete example in the codebase.

---

## Table of Contents

1. [Architectural Patterns](#1-architectural-patterns)
2. [Presentation Layer Patterns](#2-presentation-layer-patterns)
3. [Domain Layer Patterns](#3-domain-layer-patterns)
4. [Data Layer Patterns](#4-data-layer-patterns)
5. [Dependency Injection Patterns](#5-dependency-injection-patterns-hilt)
6. [Navigation Patterns](#6-navigation-patterns-jetpack-navigation-3)
7. [Reactive & Concurrency Patterns](#7-reactive--concurrency-patterns)
8. [Background Processing Patterns](#8-background-processing-patterns)
9. [Network & Streaming Patterns](#9-network--streaming-patterns)
10. [Security & Encryption Patterns](#10-security--encryption-patterns)
11. [Inter-App Communication Patterns](#11-inter-app-communication-patterns)
12. [UI Component Patterns](#12-ui-component-patterns)
13. [Game Logic Pattern](#13-game-logic-pattern-tic-tac-toe)

---

## 1. Architectural Patterns

### Clean Architecture (3-Layer)

**Problem solved:** Keeps business logic independent of Android framework and UI details, making it testable and replaceable.

Each feature `impl` module is divided into three layers with a strict dependency rule:

```
Presentation  →  Domain  ←  Data
```

- `presentation/` knows about `domain/` but not `data/`
- `domain/` is pure Kotlin — no Android imports
- `data/` implements `domain/` interfaces

**Reference:** [feature/note/impl](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/)

---

### Feature Module API / Impl Split

**Problem solved:** Enforces a public contract boundary so other features only couple to the API surface (routes, shared models), never to the implementation.

Every feature is two Gradle modules:

| Module | Contains | Allowed dependencies |
|--------|----------|----------------------|
| `feature/{name}/api` | `@Serializable` route objects, shared enums | `navigation3-runtime`, `kotlinx-serialization` |
| `feature/{name}/impl` | All presentation, domain, data, DI | `:feature:{name}:api` + core modules |

```kotlin
// feature/note/api — the only thing other modules can see
@Serializable data object NoteListRoute : NavKey

@Serializable data class NoteDetailRoute(val noteId: Long? = null) : NavKey
```

**Reference:** [feature/note/api](../feature/note/api/src/main/kotlin/com/example/android/playground/note/api/)

---

## 2. Presentation Layer Patterns

### MVI (Model-View-Intent)

**Problem solved:** Gives the screen a single, predictable source of truth (`State`), a typed vocabulary for user actions (`Intent`), and a safe channel for one-time events (`SideEffect`), eliminating inconsistent UI states.

Three contracts per screen, each in its own file:

| Contract | Kind | Location |
|----------|------|----------|
| `{Feature}State` | `data class` — immutable snapshot of everything the UI needs | `presentation/state/` |
| `{Feature}Intent` | `sealed interface` — all possible user actions | `presentation/intent/` |
| `{Feature}SideEffect` | `sealed interface` — one-time events (navigation, toasts) | `presentation/sideeffect/` |

```kotlin
// State — immutable data class with sensible defaults
data class NoteListState(
    val notes: List<NoteListItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
)

// Intent — sealed interface; data objects for no-arg actions, data classes for args
sealed interface NoteListIntent {
    data object LoadNotes : NoteListIntent
    data class SearchNotes(val query: String) : NoteListIntent
    data class DeleteNote(val noteId: Long) : NoteListIntent
    data class NavigateToDetail(val noteId: Long) : NoteListIntent
    data object NavigateToAdd : NoteListIntent
    data object NavigateBack : NoteListIntent
}

// SideEffect — one-shot events delivered via Channel<T>
sealed interface NoteListSideEffect {
    data class NavigateToNoteDetail(val noteId: Long) : NoteListSideEffect
    data object NavigateToAddNote : NoteListSideEffect
    data object NavigateBack : NoteListSideEffect
    data class ShowSuccessMessage(val message: String) : NoteListSideEffect
    data class ShowErrorMessage(val message: String) : NoteListSideEffect
}
```

**References:**
- [NoteListState.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/state/NoteListState.kt)
- [NoteListIntent.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/intent/NoteListIntent.kt)
- [NoteListSideEffect.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/sideeffect/NoteListSideEffect.kt)

---

### Screen / Content Split

**Problem solved:** Decouples lifecycle concerns (ViewModel injection, `LaunchedEffect` side-effect collection) from the pure Compose tree, making components independently previewable.

Two composable files per screen:

| File | Responsibility |
|------|---------------|
| `{Feature}Screen.kt` in `presentation/screen/` | Entry point — calls `hiltViewModel()`, collects state, handles side effects in `LaunchedEffect`, delegates all UI to Content |
| `{Feature}Content.kt` in `presentation/component/` | Pure composable — receives `state` + `onIntent` lambda; has its own `@Preview`; no ViewModel |

```kotlin
// Screen — lifecycle owner
@Composable
fun NoteListScreen(
    onNavigateBack: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is NoteListSideEffect.NavigateBack -> onNavigateBack()
                // …
            }
        }
    }

    NoteListContent(state = state, onIntent = viewModel::handleIntent)
}

// Content — pure UI, fully previewable
@Composable
internal fun NoteListContent(
    state: NoteListState,
    onIntent: (NoteListIntent) -> Unit,
) { /* … */ }
```

**References:**
- [NoteListScreen.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/screen/NoteListScreen.kt)
- [NoteListContent.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/component/NoteListContent.kt)

---

### ViewModel Intent Dispatch (handleIntent)

**Problem solved:** Provides a single, stable public API for the UI layer to send intents to the ViewModel, regardless of how many internal private functions exist.

```kotlin
@HiltViewModel
class NoteListViewModel @Inject constructor(…) : ViewModel() {

    fun handleIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.LoadNotes    -> refreshNotes()
            is NoteListIntent.SearchNotes  -> searchNotes(intent.query)
            is NoteListIntent.DeleteNote   -> deleteNote(intent.noteId)
            is NoteListIntent.NavigateToDetail -> navigateToDetail(intent.noteId)
            is NoteListIntent.NavigateToAdd    -> navigateToAdd()
            is NoteListIntent.NavigateBack     -> navigateBack()
        }
    }
    // all private below …
}
```

The Content composable calls `onIntent(NoteListIntent.DeleteNote(id))` — it never reaches into the ViewModel directly.

**Reference:** [NoteListViewModel.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/presentation/viewmodel/NoteListViewModel.kt)

---

## 3. Domain Layer Patterns

### Use Case — Operator Invoke

**Problem solved:** Encapsulates a single business operation in a named class that reads like a function call (`getNotesUseCase()`), while still being injectable and individually testable.

```kotlin
class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}
```

Rules followed across all features:
- One use case per file, named after the action (`GetNoteByIdUseCase`, `DeleteNoteUseCase`, etc.)
- Returns `Flow<T>` for reads (stream of updates), `suspend` function for mutations
- Depends only on the repository *interface*, never on the impl

**Reference:** [GetNotesUseCase.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/usecase/GetNotesUseCase.kt)

---

### Repository Interface (Dependency Inversion)

**Problem solved:** Keeps the domain layer free of data-source details (Room, network, DataStore). The domain defines what it needs; data fulfils it.

```kotlin
// In domain/ — the contract
interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNoteContent(id: Long, title: String, content: String)
    suspend fun deleteNoteById(id: Long)
    suspend fun getNoteCount(): Int
}

// In data/ — the implementation, invisible to domain
@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
) : NoteRepository { … }
```

**References:**
- [NoteRepository.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/domain/repository/NoteRepository.kt)
- [NoteRepositoryImpl.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/repository/NoteRepositoryImpl.kt)

---

## 4. Data Layer Patterns

### Repository Implementation — Entity/Domain Mapping

**Problem solved:** Translates persistence types (Room entities) to clean domain models, keeping the domain layer free of database annotations.

The repository uses extension functions declared alongside the entity:

```kotlin
// Mapping extension functions (in data/local/)
fun NoteEntity.toDomainModel(): Note = Note(id = id, title = title, …)
fun List<NoteEntity>.toDomainModelList(): List<Note> = map { it.toDomainModel() }
fun Note.toEntity(): NoteEntity = NoteEntity(id = id, title = title, …)

// Repository delegates to DAO and maps the result
override fun getAllNotes(): Flow<List<Note>> =
    noteDao.getAllNotes().map { entities -> entities.toDomainModelList() }
```

**Reference:** [NoteRepositoryImpl.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/repository/NoteRepositoryImpl.kt)

---

### DAO Pattern (Room)

**Problem solved:** Abstracts SQL into type-safe Kotlin interfaces; Room generates the implementation at compile time.

| Query type | Return type | Use |
|-----------|-------------|-----|
| Reads (observed) | `Flow<List<T>>` | Auto-updates UI when data changes |
| Reads (one-shot) | `suspend fun` | Single lookup |
| Writes | `suspend fun` | Insert / update / delete |

```kotlin
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updated_at DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}
```

**References:**
- [NoteDao.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/data/local/NoteDao.kt)
- [LibraryDao.kt](../feature/room-database/impl/src/main/kotlin/com/example/android/playground/roomdatabase/data/local/LibraryDao.kt)

---

### Room Relationships

**Problem solved:** Models one-to-many and many-to-many relationships in a relational database without loading entire object graphs at once.

Patterns used in `feature/room-database`:

| Technique | Use case |
|-----------|----------|
| `@Relation` + `@Embedded` | One Author → many Books (`AuthorWithBooksRelation`) |
| Cross-ref join table | Many Books ↔ many Tags (`BookTagCrossRef`) |
| `@TypeConverter` | Custom enum (`Genre`) serialised to/from `String` in `GenreTypeConverter` |
| `@Transaction` | Ensures atomic load of parent + children |

```kotlin
data class AuthorWithBooksRelation(
    @Embedded val author: AuthorEntity,
    @Relation(parentColumn = "id", entityColumn = "author_id")
    val books: List<BookEntity>,
)
```

**Reference:** [feature/room-database/impl](../feature/room-database/impl/src/main/kotlin/com/example/android/playground/roomdatabase/data/local/)

---

### Encrypted DataStore

**Problem solved:** Stores sensitive key-value data (tokens, credentials) with hardware-backed encryption, avoiding plain `SharedPreferences`.

Two complementary approaches in `feature/crypto-security`:

| Class | Backing technology |
|-------|--------------------|
| `KeystoreDataStoreManager` | Android Keystore AES key + `DataStore<Preferences>` |
| `TinkDataStoreManager` | Google Tink `KeysetHandle` + `DataStore<Preferences>` |

**Reference:** [feature/crypto-security/impl/data/crypto/](../feature/crypto-security/impl/src/main/kotlin/com/example/android/playground/cryptosecurity/data/crypto/)

---

## 5. Dependency Injection Patterns (Hilt)

### @Binds — Interface-to-Implementation Binding

**Problem solved:** Tells Hilt which concrete class satisfies a given interface, with zero runtime overhead (compile-time delegate generation).

```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
    // …
}
```

`@Binds` is preferred over `@Provides` here because no construction logic is needed — Hilt already knows how to build `NoteRepositoryImpl`.

**Reference:** [NoteModule.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/di/NoteModule.kt)

---

### @Provides with Companion Object (Builder Pattern)

**Problem solved:** Groups related factory methods inside an interface module; `companion object` holds `@Provides` functions while the interface itself holds `@Binds` functions.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    companion object {
        @Provides @Singleton
        fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
            Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, NoteDatabase.DATABASE_NAME)
                .build()

        @Provides
        fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
    }
}
```

Room's own `databaseBuilder` is an example of the **Builder** pattern embedded inside DI setup.

**Reference:** [NoteModule.kt](../feature/note/impl/src/main/kotlin/com/example/android/playground/note/di/NoteModule.kt)

---

### @IntoSet Multibinding — Navigation Registration

**Problem solved:** Lets each feature register its own screen entries without the `app` module knowing about them. `AppNavigation` collects the full `Set<EntryProviderInstaller>` and renders all screens declaratively.

```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
object NoteNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller = {
        entry<NoteListRoute> { NoteListScreen(onNavigateBack = { navigator.goBack() }) }
        entry<NoteDetailRoute> { NoteDetailScreen(onNavigateBack = { navigator.goBack() }) }
    }
}
```

Every feature contributes to the same `Set<EntryProviderInstaller>` — zero coupling between feature modules.

**Reference:** [FeedNavigationModule.kt](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/di/FeedNavigationModule.kt)

---

### @Named Qualifier

**Problem solved:** Disambiguates multiple bindings of the same type (e.g., two `OkHttpClient` instances with different configurations).

```kotlin
@Provides
@Named("websocket")
fun provideWebSocketOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .readTimeout(0, TimeUnit.MILLISECONDS)  // no timeout for persistent connections
    .build()
```

At the injection site:
```kotlin
class BinanceWebSocketSource @Inject constructor(
    @Named("websocket") private val okHttpClient: OkHttpClient,
    @Named("websocket") private val json: Json,
)
```

**Reference:** [feature/websocket/impl/di/WebSocketModule.kt](../feature/websocket/impl/src/main/kotlin/com/example/android/playground/websocket/di/WebSocketModule.kt)

---

### @HiltWorker + @AssistedInject

**Problem solved:** Injects dependencies into WorkManager `Worker` classes, which are constructed by the WorkManager framework (not Hilt), by using Dagger's `@AssistedInject` for framework-provided parameters.

```kotlin
@HiltWorker
class MediaUploadOrchestratorWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MediaRepository,    // ← injected by Hilt
) : CoroutineWorker(appContext, workerParams)
```

**Reference:** [MediaUploadOrchestratorWorker.kt](../feature/media-orchestrator/impl/src/main/kotlin/com/example/android/playground/mediaorchestrator/data/worker/MediaUploadOrchestratorWorker.kt)

---

### @AndroidEntryPoint — Field Injection in Android Components

**Problem solved:** Enables constructor injection to be replaced by field injection in Android framework classes (`JobService`, `Service`, `BroadcastReceiver`) that the OS constructs.

```kotlin
@AndroidEntryPoint
class UserInitiatedTransferJobService : JobService() {
    @Inject lateinit var repository: TransferRepository
    // …
}
```

**Reference:** [UserInitiatedTransferJobService.kt](../feature/user-initiated-service/impl/src/main/kotlin/com/example/android/playground/userinitiatedservice/data/job/UserInitiatedTransferJobService.kt)

---

### Component Scoping Summary

| Scope | Lifetime | Used for |
|-------|----------|----------|
| `SingletonComponent` | App process | Database, Repositories, Network clients |
| `ActivityRetainedComponent` | Activity (survives rotation) | `AppNavigator`, NavigationModules |
| `ViewModelComponent` | ViewModel | `@HiltViewModel` ViewModels |
| Worker | Worker execution | `@HiltWorker` |

---

## 6. Navigation Patterns (Jetpack Navigation 3)

### Type-Safe Routes as NavKey

**Problem solved:** Eliminates string-based route arguments and runtime crashes from missing/mistyped parameters by using Kotlin's type system and `@Serializable`.

```kotlin
// No arguments — data object
@Serializable
data object NoteListRoute : NavKey

// With arguments — data class
@Serializable
data class NoteDetailRoute(val noteId: Long? = null) : NavKey
```

Routes live in the `api` module so the `app` module and `FeedNavigationModule` can reference them without importing `impl`.

**Reference:** [feature/note/api](../feature/note/api/src/main/kotlin/com/example/android/playground/note/api/)

---

### Back Stack as SnapshotStateList (Observer Pattern)

**Problem solved:** Makes the navigation back stack a first-class Compose observable so `NavDisplay` automatically recomposes when the stack changes.

```kotlin
@ActivityRetainedScoped
class AppNavigator(startDestination: NavKey) {
    val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startDestination)

    fun goTo(destination: NavKey) { backStack.add(destination) }
    fun goBack()                  { backStack.removeLastOrNull() }
}
```

`SnapshotStateList` is Compose's reactive list — reads inside a composable automatically register as snapshot observers.

**Reference:** [AppNavigator.kt](../core/navigation/src/main/kotlin/com/example/android/playground/core/navigation/AppNavigator.kt)

---

### Route Dispatch Table (Map Pattern)

**Problem solved:** Avoids a long `when` chain for mapping topic IDs to routes, making it trivial to add or remove a topic by editing a single map entry.

```kotlin
private val topicRoutes: Map<TopicId, NavKey> = mapOf(
    TopicId.NoteApp              to NoteListRoute,
    TopicId.ImageUploadApp       to ImageUploadRoute,
    TopicId.MediaOrchestratorApp to MediaOrchestratorRoute,
    TopicId.WebSocket            to WebSocketRoute,
    TopicId.GraphQL              to GraphQLRoute,
    // …14 entries total
)

// Usage — null-safe lookup
onTopicClick = { topicId -> topicRoutes[topicId]?.let { navigator.goTo(it) } }
```

**Reference:** [FeedNavigationModule.kt](../feature/feed/impl/src/main/kotlin/com/example/android/playground/feed/di/FeedNavigationModule.kt)

---

## 7. Reactive & Concurrency Patterns

### StateFlow for UI State

**Problem solved:** Provides a lifecycle-aware, always-current state holder that survives recomposition and re-delivers the last value to new subscribers.

```kotlin
private val _state = MutableStateFlow(NoteListState(isLoading = true))
val state: StateFlow<NoteListState> = _state.asStateFlow()
```

Collected in the Screen composable with lifecycle awareness:
```kotlin
val state by viewModel.state.collectAsStateWithLifecycle()
```

---

### Channel + receiveAsFlow for SideEffects

**Problem solved:** Delivers exactly-once navigation/toast events that must not be replayed if the subscriber re-subscribes (unlike `StateFlow` which replays the last value).

```kotlin
private val _sideEffect = Channel<NoteListSideEffect>(Channel.BUFFERED)
val sideEffect = _sideEffect.receiveAsFlow()

// Emission (ViewModel)
viewModelScope.launch { _sideEffect.send(NoteListSideEffect.NavigateBack) }

// Collection (Screen)
LaunchedEffect(Unit) {
    viewModel.sideEffect.collect { effect -> /* handle */ }
}
```

`Channel.BUFFERED` means the ViewModel can emit while the Screen is briefly inactive (config change) without suspending or losing the event.

---

### callbackFlow — Bridging Callbacks to Flow

**Problem solved:** Adapts event-listener/callback APIs (OkHttp `WebSocketListener`, OkHttp SSE listener) to Kotlin Flows, enabling structured concurrency and backpressure.

```kotlin
fun observeTicker(): Flow<WebSocketEvent> = callbackFlow {
    trySend(WebSocketEvent.Connecting)

    val listener = object : WebSocketListener() {
        override fun onOpen(ws: WebSocket, response: Response) {
            trySend(WebSocketEvent.Connected)
        }
        override fun onMessage(ws: WebSocket, text: String) {
            val ticker = json.decodeFromString<BinanceTickerDto>(text)
            trySend(WebSocketEvent.Message(ticker.toDomain()))
        }
        override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
            trySend(WebSocketEvent.Error(t))
            close(t)
        }
        override fun onClosed(ws: WebSocket, code: Int, reason: String) {
            trySend(WebSocketEvent.Disconnected)
            channel.close()
        }
    }

    val ws = okHttpClient.newWebSocket(Request.Builder().url(BINANCE_WS_URL).build(), listener)

    awaitClose { ws.close(WS_CLOSE_NORMAL, "Flow cancelled") }
}
```

`awaitClose` ensures the WebSocket is closed when the flow collector cancels.

**Reference:** [BinanceWebSocketSource.kt](../feature/websocket/impl/src/main/kotlin/com/example/android/playground/websocket/data/source/BinanceWebSocketSource.kt)

---

### Flow Operator Composition

**Problem solved:** Builds complex reactive pipelines declaratively, avoiding manual threading code.

Key operator uses across the project:

| Operator | Where used | Purpose |
|----------|-----------|---------|
| `flatMapLatest` | `NoteListViewModel` | Re-fetch notes every time `loadTrigger` fires; cancels previous fetch |
| `combine` | `NoteListViewModel`, `RoomDatabaseViewModel` | Merge multiple independent flows into a single state update |
| `debounce` | `NoteListViewModel` | Delay search query 300 ms to avoid querying on every keystroke |
| `stateIn(WhileSubscribed)` | All ViewModels | Convert cold flow to hot `StateFlow`; stops upstream when no collectors |
| `map` | Repository impls | Transform entity lists to domain models inside the flow |
| `catch` | `NoteListViewModel` | Emit a fallback value and surface error via SideEffect without crashing |

```kotlin
// NoteListViewModel — load + search pipeline
private val notesData = loadTrigger
    .flatMapLatest { getNotesUseCase()
        .map { notes -> withContext(Dispatchers.Default) { notes.map(NoteUiMapper::toListUiModel) } }
        .catch { e -> _sideEffect.send(ShowErrorMessage(…)); emit(emptyList()) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

private val filteredNotes = combine(notesData, debouncedSearchQuery) { notes, query ->
    if (query.isNotBlank()) withContext(Dispatchers.Default) { notes.filter { … } } else notes
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
```

---

### Dispatcher Switching

**Problem solved:** Offloads CPU-intensive work (list filtering, mapping) from the main thread without manual `Thread` management.

```kotlin
// Inside a flow operator — switches to Default pool for the computation, then returns
.map { notes ->
    withContext(Dispatchers.Default) {
        notes.map { note -> NoteUiMapper.toListUiModel(note) }
    }
}
```

`Dispatchers.Default` is used for CPU-bound work; `Dispatchers.IO` is used in JobService (`CoroutineScope(SupervisorJob() + Dispatchers.IO)`).

---

### Coroutine Semaphore — Bounded Concurrency

**Problem solved:** Limits how many coroutines execute in parallel (e.g., upload workers) to avoid saturating the network or exceeding OS limits.

```kotlin
val semaphore = Semaphore(MAX_CONCURRENT_UPLOADS) // = 3
coroutineScope {
    pendingItems.forEach { item ->
        launch {
            semaphore.withPermit { uploadItemWithChunkResume(item) }
        }
    }
}
```

All `launch` calls fire immediately, but only 3 run concurrently — the rest suspend on `withPermit` until a slot is freed.

**Reference:** [MediaUploadOrchestratorWorker.kt](../feature/media-orchestrator/impl/src/main/kotlin/com/example/android/playground/mediaorchestrator/data/worker/MediaUploadOrchestratorWorker.kt)

---

### SupervisorJob — Isolated Failure Propagation

**Problem solved:** Prevents one child coroutine's failure from cancelling all sibling coroutines — critical when processing independent items in parallel or in a long-lived Android service.

```kotlin
// UserInitiatedTransferJobService
private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
```

With a normal `Job`, a failed transfer item would cancel all other transfers. `SupervisorJob` restricts the failure to just that child.

**Reference:** [UserInitiatedTransferJobService.kt](../feature/user-initiated-service/impl/src/main/kotlin/com/example/android/playground/userinitiatedservice/data/job/UserInitiatedTransferJobService.kt)

---

## 8. Background Processing Patterns

### CoroutineWorker with Chunk-Based Resume (WorkManager)

**Problem solved:** Runs long background work (bulk media upload) that survives process death by checkpointing progress to the database after every chunk, so the worker resumes from the last committed chunk rather than restarting from zero.

Key design decisions in `MediaUploadOrchestratorWorker`:

1. **Single worker per batch** — avoids WorkManager chain-cancellation issues that arise with per-item workers
2. **Semaphore(3) concurrency cap** — bounded parallel uploads
3. **DB as the sole state store** — `uploadedChunks` written after every chunk; re-run reads this and skips completed chunks
4. **`Result.success()` always** — individual item failures are recorded in DB as `FAILED`; returning `Result.failure()` would trigger a full batch retry

```kotlin
override suspend fun doWork(): Result {
    setForeground(createForegroundInfo())
    val semaphore = Semaphore(MAX_CONCURRENT_UPLOADS)
    coroutineScope {
        repository.getPendingItems().forEach { item ->
            launch { semaphore.withPermit { uploadItemWithChunkResume(item) } }
        }
    }
    return Result.success()
}

private suspend fun uploadItemWithChunkResume(item: MediaItem) {
    val startChunk = item.uploadedChunks   // resume point from DB
    for (chunk in startChunk until item.totalChunks) {
        delay(CHUNK_DELAY)
        repository.updateChunkProgress(id = item.id, uploadedChunks = chunk + 1)
    }
    repository.updateStatus(id = item.id, status = UploadStatus.SUCCESS)
}
```

**Reference:** [MediaUploadOrchestratorWorker.kt](../feature/media-orchestrator/impl/src/main/kotlin/com/example/android/playground/mediaorchestrator/data/worker/MediaUploadOrchestratorWorker.kt)

---

### Foreground Service Type — API-Level Branching

**Problem solved:** Uses the most appropriate foreground service type on each Android version, so the OS grants the maximum allowed execution time while remaining compliant with background execution restrictions.

```kotlin
private fun createForegroundInfo(): ForegroundInfo = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM ->
        // API 35+ — mediaProcessing: up to 6 hours for upload/processing tasks
        ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING)

    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
        // API 29–34 — dataSync: closest applicable type
        ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)

    else ->
        // API 28 (minSdk) — no type argument available
        ForegroundInfo(NOTIFICATION_ID, notification)
}
```

This `when` on `Build.VERSION.SDK_INT` is a pervasive pattern for graceful degradation across API levels.

**Reference:** [MediaUploadOrchestratorWorker.kt](../feature/media-orchestrator/impl/src/main/kotlin/com/example/android/playground/mediaorchestrator/data/worker/MediaUploadOrchestratorWorker.kt)

---

### User-Initiated JobService (API 34+)

**Problem solved:** Makes long-running data transfers visible in the system Task Manager (Android 14+), giving users explicit control to monitor and cancel jobs — solving the trust and transparency problem of silent background services.

Differentiators from `WorkManager`:
- Appears in Task Manager long-press Recents; user can tap **Stop** which calls `onStopJob`
- ~10-minute grace period before OS enforces visibility constraints (vs 10 seconds for foreground service)
- `JobScheduler` validates that `schedule()` is called within a user-gesture call stack — prevents misuse from background components

```kotlin
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)  // API 34
@AndroidEntryPoint
class UserInitiatedTransferJobService : JobService() {
    @Inject lateinit var repository: TransferRepository
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onStartJob(params: JobParameters): Boolean {
        serviceScope.launch { runTransfer(params) }
        return true  // async — job continues after return
    }

    override fun onStopJob(params: JobParameters): Boolean {
        transferJob?.cancel()
        serviceScope.launch {
            repository.getRunningItems().forEach { repository.updateStatus(it.id, TransferStatus.CANCELLED) }
        }
        return true  // reschedule when conditions improve
    }
}
```

The project also ships `TransferWorkerLegacy` (WorkManager) as the equivalent implementation for API < 34, illustrating an **API-level feature parity pattern**.

**Reference:** [UserInitiatedTransferJobService.kt](../feature/user-initiated-service/impl/src/main/kotlin/com/example/android/playground/userinitiatedservice/data/job/UserInitiatedTransferJobService.kt)

---

## 9. Network & Streaming Patterns

### WebSocket — callbackFlow + OkHttp

**Problem solved:** Integrates a persistent bidirectional WebSocket connection with Kotlin coroutines, providing a clean `Flow<WebSocketEvent>` API to the domain layer.

The `WebSocketEvent` sealed class is a typed event model:
```kotlin
sealed interface WebSocketEvent {
    data object Connecting : WebSocketEvent
    data object Connected : WebSocketEvent
    data class Message(val ticker: BtcTicker) : WebSocketEvent
    data class Error(val throwable: Throwable) : WebSocketEvent
    data object Disconnected : WebSocketEvent
}
```

The `callbackFlow` in `BinanceWebSocketSource` bridges OkHttp's listener callbacks to this sealed hierarchy. The domain `ObserveBtcTickerUseCase` wraps the repository flow; the ViewModel collects it and maps to `WebSocketState`.

**Reference:** [feature/websocket/impl](../feature/websocket/impl/src/main/kotlin/com/example/android/playground/websocket/)

---

### SSE (Server-Sent Events)

**Problem solved:** Consumes a unidirectional real-time event stream from an HTTP endpoint using OkHttp's built-in SSE support, wrapped in a Flow.

OkHttp's `EventSource` / `EventSourceListener` is bridged via `callbackFlow` in the same style as the WebSocket pattern, giving the domain layer a `Flow<SseEvent>`.

**Reference:** [feature/sse/impl](../feature/sse/impl/src/main/kotlin/com/example/android/playground/sse/)

---

### gRPC Streaming

**Problem solved:** Communicates with a gRPC server using bidirectional streaming (Eliza chatbot demo), with Kotlin coroutines handling the async send/receive loop.

```
ElizaGrpcDataSource  →  GrpcRepositoryImpl  →  GrpcRepository (interface)
                                                   ↑
                                           SendElizaMessageUseCase
```

`ElizaGrpcDataSource` uses the generated `ElizaServiceClient` (gRPC Kotlin stubs) and exposes a `Flow<ElizaMessage>`.

**Reference:** [feature/grpc/impl](../feature/grpc/impl/src/main/kotlin/com/example/android/playground/grpc/)

---

### GraphQL — Dual Data Source Mode (Strategy Pattern)

**Problem solved:** Demonstrates two ways to execute a GraphQL query — Apollo Kotlin's type-safe generated API vs a raw HTTP request with manual JSON parsing — switchable at runtime.

```kotlin
// Strategy selection model
enum class DataSourceMode { Apollo, RawHttp }

// Two use cases, each encapsulating one strategy
class ApolloSearchReposUseCase @Inject constructor(…)
class RawSearchReposUseCase   @Inject constructor(…)
```

The ViewModel dispatches to the appropriate use case based on the current `DataSourceMode`, which the user can toggle in the UI. This is the **Strategy** pattern: same operation, two interchangeable implementations.

**Reference:** [feature/graphql/impl](../feature/graphql/impl/src/main/kotlin/com/example/android/playground/graphql/)

---

## 10. Security & Encryption Patterns

### AES-GCM with Android Keystore

**Problem solved:** Encrypts sensitive data using a hardware-backed symmetric key that never leaves the secure enclave, preventing key extraction even on rooted devices.

`AesGcmCryptoManager` generates or retrieves an AES-256-GCM key in the Android Keystore and provides `encrypt(plaintext)` / `decrypt(ciphertext)` operations.

**Reference:** [feature/crypto-security/impl/data/crypto/AesGcmCryptoManager.kt](../feature/crypto-security/impl/src/main/kotlin/com/example/android/playground/cryptosecurity/data/crypto/AesGcmCryptoManager.kt)

---

### RSA Encryption with Android Keystore

**Problem solved:** Demonstrates asymmetric encryption using a Keystore-resident RSA key pair for use cases such as signing tokens or encrypting a symmetric key.

**Reference:** [feature/crypto-security/impl/data/crypto/RsaCryptoManager.kt](../feature/crypto-security/impl/src/main/kotlin/com/example/android/playground/cryptosecurity/data/crypto/RsaCryptoManager.kt)

---

### Google Tink Encrypted DataStore

**Problem solved:** Uses Google Tink's high-level API to encrypt DataStore contents with minimal boilerplate, abstracting away key generation and rotation.

`TinkDataStoreManager` wraps `DataStore<Preferences>` with Tink's `Aead` primitive, which handles nonce generation, AEAD authentication, and key management automatically.

**Reference:** [feature/crypto-security/impl/data/crypto/TinkDataStoreManager.kt](../feature/crypto-security/impl/src/main/kotlin/com/example/android/playground/cryptosecurity/data/crypto/TinkDataStoreManager.kt)

---

### Simulated Secure Network / Certificate Pinning

**Problem solved:** Demonstrates how to configure OkHttp for certificate pinning and mutual TLS without requiring a real production backend, using a `FakeSecureApiService` that simulates the server side.

**Reference:** [feature/crypto-security/impl/data/service/FakeSecureApiService.kt](../feature/crypto-security/impl/src/main/kotlin/com/example/android/playground/cryptosecurity/data/service/FakeSecureApiService.kt)

---

## 11. Inter-App Communication Patterns

The `feature/inter-app-comm` module is structured as a mini-app with a home screen that routes to five sub-screens, each demonstrating a distinct IPC mechanism. All five follow the same MVI + Screen/Content pattern.

### AIDL (Android Interface Definition Language)

**Problem solved:** Enables strongly-typed, synchronous method calls across process boundaries, backed by Binder. Suitable for exposing a rich API from a service to other apps.

`AidlScreen` + `AidlViewModel` demonstrate binding to a remote service, calling its methods, and handling `RemoteException`.

---

### Messenger

**Problem solved:** Lighter-weight alternative to AIDL for one-directional or loosely coupled cross-process messaging. The client sends `Message` objects to the service's `Handler` without needing a shared `.aidl` file.

`MessengerScreen` demonstrates binding, sending messages with `Messenger.send()`, and receiving replies via a client-side `Handler`.

---

### BroadcastReceiver (LocalBroadcastManager / System Broadcasts)

**Problem solved:** Decoupled pub/sub within the app or between apps; no persistent connection required. The sender and receiver do not need references to each other.

`BroadcastScreen` demonstrates both sending custom intents and receiving system broadcasts.

---

### ContentProvider

**Problem solved:** Exposes structured data (like a mini-database) to other apps with fine-grained URI-based access control enforced by the OS permission system.

`ContentProviderScreen` demonstrates querying a content URI and observing changes with a `ContentObserver`.

---

### Explicit Intent

**Problem solved:** Directly launches a known Activity in another app by specifying its component name — the simplest form of cross-app navigation.

`ExplicitIntentScreen` demonstrates composing an `Intent` with a fully-qualified component name and handling `ActivityNotFoundException`.

**Reference:** [feature/inter-app-comm/impl](../feature/inter-app-comm/impl/src/main/kotlin/com/example/android/playground/interappcomm/)

---

## 12. UI Component Patterns

### Custom Multi-Preview Annotations

**Problem solved:** Reduces preview boilerplate and ensures every component is consistently previewed in both light and dark themes with a single annotation.

```kotlin
// In core/ui/preview/PreviewAnnotations.kt
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark",  uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DualThemePreview

@Preview(showBackground = true, widthDp = 360)
annotation class ComponentPreview

@Preview(showSystemUi = true, device = Devices.PIXEL_6)
annotation class FullPreview
```

Usage at the bottom of every component file:
```kotlin
@DualThemePreview
@Composable
private fun NoteItemPreview() {
    PreviewContainer { NoteItem(note = previewNote, onDelete = {}) }
}
```

**Reference:** [core/ui/src/.../preview/](../core/ui/src/main/kotlin/com/example/android/playground/core/ui/preview/)

---

### PreviewContainer

**Problem solved:** Wraps preview composables in the app's `MaterialTheme` so previews reflect real theme colors and typography without repeating theme setup in every preview function.

```kotlin
@Composable
fun PreviewContainer(content: @Composable () -> Unit) {
    AndroidPlaygroundTheme { content() }
}
```

**Reference:** [core/ui/src/.../preview/PreviewUtils.kt](../core/ui/src/main/kotlin/com/example/android/playground/core/ui/preview/PreviewUtils.kt)

---

### One-Component-per-File Rule

**Problem solved:** Keeps files focused and searchable; every component can be found and updated without navigating a large multi-component file.

All reusable composables live under `presentation/component/` — one file per composable:
`NoteItem.kt`, `NoteSearchBar.kt`, `NoteErrorCard.kt`, `MediaItemCard.kt`, `StatusChip.kt`, `ElegantSeekBar.kt`, …

---

### Analytics Tracking — TrackScreenViewEvent

**Problem solved:** Ensures every screen fires an analytics event when it enters the composition, with no risk of forgetting to add the call because it is baked into every screen template.

```kotlin
@Composable
fun NoteListScreen(…) {
    TrackScreenViewEvent(screenName = NoteConstants.SCREEN_NAME)
    // …
}
```

`TrackScreenViewEvent` is a `DisposableEffect`-based composable from `core/common` that fires once on entry and cleans up on exit.

**Reference:** [core/common](../core/common/src/main/kotlin/com/example/android/playground/common/)

---

### Shared AppTopAppBar Component

**Problem solved:** Provides a single consistent top app bar across all screens, eliminating per-screen copy-paste of `TopAppBar` configuration.

```kotlin
AppTopAppBar(
    title = "Notes",
    onNavigateBack = { onIntent(NoteListIntent.NavigateBack) },
)
```

**Reference:** [core/ui/src/.../components/AppTopAppBar.kt](../core/ui/src/main/kotlin/com/example/android/playground/core/ui/components/AppTopAppBar.kt)

---

## 13. Game Logic Pattern (Tic-Tac-Toe)

### Multi-Screen Feature with Independent MVI Triads

**Problem solved:** A feature requiring a setup flow followed by a game loop is split into two entirely independent screens — each with its own `State`, `Intent`, `SideEffect`, and `ViewModel` — avoiding a single bloated ViewModel that handles both phases.

```
TicTacToeSetupScreen  →  (SideEffect: StartGame)  →  TicTacToeGameScreen
     ↓                                                      ↓
TicTacToeSetupState                               TicTacToeGameState
TicTacToeSetupIntent                              TicTacToeGameIntent
TicTacToeSetupSideEffect                          TicTacToeGameSideEffect
TicTacToeSetupViewModel                           TicTacToeGameViewModel
```

### Pure Domain Logic in ViewModel

**Problem solved:** Game rules (win detection, board state, player turns, move validation) are pure Kotlin logic with no Android dependencies, making them unit-testable without an Android emulator.

The `TicTacToeGameViewModel` holds the board as part of `TicTacToeGameState` and evaluates win conditions inline — no `android.*` imports needed in that logic path.

Game results are persisted to a Room database (`GameResultEntity`, `GameResultDao`) so history can be reviewed — connecting the game logic pattern with the DAO pattern.

**Reference:** [feature/tic-tac-toe/impl](../feature/tic-tac-toe/impl/src/main/kotlin/com/example/android/playground/tictactoe/)

---

## Quick Reference — Pattern Index

| Pattern | Category | Key file(s) |
|---------|----------|-------------|
| Clean Architecture (3-layer) | Architecture | All `feature/*/impl` |
| API / Impl module split | Architecture | `feature/*/api`, `feature/*/impl` |
| MVI — State/Intent/SideEffect | Presentation | `presentation/{state,intent,sideeffect}/` |
| Screen / Content split | Presentation | `*Screen.kt`, `*Content.kt` |
| `handleIntent()` dispatch | Presentation | `*ViewModel.kt` |
| Use Case — `operator fun invoke()` | Domain | `domain/usecase/*.kt` |
| Repository interface | Domain | `domain/repository/*.kt` |
| Entity ↔ Domain mappers (extension fns) | Data | `data/local/*Mapper.kt` |
| DAO (Room) | Data | `data/local/*Dao.kt` |
| Room Relationships / TypeConverter | Data | `feature/room-database/impl` |
| Encrypted DataStore | Data | `feature/crypto-security/impl/data/crypto/` |
| `@Binds` binding | DI | `di/*Module.kt` |
| `@Provides` + Room Builder | DI | `di/*Module.kt` companion |
| `@IntoSet` multibinding | DI | `di/*NavigationModule.kt` |
| `@Named` qualifier | DI | `di/WebSocketModule.kt` |
| `@HiltWorker` + `@AssistedInject` | DI | `*Worker.kt` |
| `@AndroidEntryPoint` field injection | DI | `*JobService.kt` |
| Type-safe NavKey routes | Navigation | `feature/*/api/*.kt` |
| `SnapshotStateList` back stack | Navigation | `AppNavigator.kt` |
| Route dispatch table (Map) | Navigation | `FeedNavigationModule.kt` |
| `StateFlow` for UI state | Reactive | `*ViewModel.kt` |
| `Channel<T>` for SideEffects | Reactive | `*ViewModel.kt` |
| `callbackFlow` | Reactive | `*WebSocketSource.kt`, SSE data sources |
| `flatMapLatest`, `combine`, `debounce` | Reactive | `NoteListViewModel.kt` |
| `Semaphore` bounded concurrency | Reactive | `MediaUploadOrchestratorWorker.kt` |
| `SupervisorJob` | Reactive | `UserInitiatedTransferJobService.kt` |
| `CoroutineWorker` + chunk resume | Background | `MediaUploadOrchestratorWorker.kt` |
| Foreground service API-level branching | Background | `MediaUploadOrchestratorWorker.kt` |
| User-Initiated JobService | Background | `UserInitiatedTransferJobService.kt` |
| WebSocket via `callbackFlow` | Network | `BinanceWebSocketSource.kt` |
| SSE Flow | Network | `feature/sse/impl` |
| gRPC streaming | Network | `feature/grpc/impl` |
| GraphQL dual-mode (Strategy) | Network | `feature/graphql/impl` |
| AES-GCM + Keystore | Security | `AesGcmCryptoManager.kt` |
| RSA + Keystore | Security | `RsaCryptoManager.kt` |
| Google Tink DataStore | Security | `TinkDataStoreManager.kt` |
| AIDL | IPC | `feature/inter-app-comm/impl` |
| Messenger | IPC | `feature/inter-app-comm/impl` |
| BroadcastReceiver | IPC | `feature/inter-app-comm/impl` |
| ContentProvider | IPC | `feature/inter-app-comm/impl` |
| Explicit Intent | IPC | `feature/inter-app-comm/impl` |
| `@DualThemePreview` / `@ComponentPreview` | UI | `core/ui/preview/` |
| `PreviewContainer` | UI | `core/ui/preview/PreviewUtils.kt` |
| `TrackScreenViewEvent` | UI | `core/common/`, every `*Screen.kt` |
| `AppTopAppBar` shared component | UI | `core/ui/components/AppTopAppBar.kt` |
| Multi-screen MVI feature (Tic-Tac-Toe) | Game | `feature/tic-tac-toe/impl` |
