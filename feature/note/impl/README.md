# Todo Feature Module

This module implements a complete Todo application using Clean Architecture principles with Android Jetpack Compose, Room database, and Hilt dependency injection.

## Architecture Overview

The module follows Clean Architecture with clear separation of concerns:

### Domain Layer
- **Models**: `Todo` - Core business entity
- **Repository Interface**: `TodoRepository` - Defines data operations contract
- **Use Cases**: Business logic operations
  - `GetTodosUseCase` - Retrieve all todos
  - `GetTodoByIdUseCase` - Retrieve specific todo
  - `InsertTodoUseCase` - Create new todo
  - `UpdateTodoUseCase` - Update existing todo
  - `DeleteTodoUseCase` - Delete todo

### Data Layer
- **Local Data Source**: Room database implementation
  - `TodoEntity` - Room entity for database table
  - `TodoDao` - Data Access Object for CRUD operations
  - `TodoDatabase` - Room database configuration
  - `TodoMapper` - Conversion between entity and domain models
- **Repository Implementation**: `TodoRepositoryImpl` - Implements domain repository interface

### Presentation Layer
- **State Management**: 
  - `TodoListState` - State for todo list screen
  - `TodoDetailState` - State for todo detail screen
- **ViewModels**: 
  - `TodoListViewModel` - Manages todo list state and operations
  - `TodoDetailViewModel` - Manages todo detail/edit state
- **UI Screens**:
  - `TodoListScreen` - List view with search and CRUD operations
  - `TodoDetailScreen` - Detail view with edit capabilities
- **Navigation**: `TodoNavigation` - Navigation setup for todo screens

### Dependency Injection
- `TodoModule` - Hilt module providing database, DAO, and repository dependencies

## Features

### Todo List Screen
- Display all todos ordered by last updated
- Search functionality (searches both title and content)
- Add new todo (FAB button)
- Delete todo (swipe or button)
- Navigate to todo details
- Loading and error states

### Todo Detail Screen
- View todo details
- Edit existing todo (title and content)
- Create new todo
- Save/Cancel operations
- Display creation and update timestamps
- Navigation back to list

### Database Features
- SQLite database using Room
- Automatic timestamps for creation and updates
- Data persistence across app sessions
- Efficient querying with Flow for reactive updates

## Usage

### Integration into Main App

1. **Add Navigation**: Include todo navigation in your main navigation graph:
```kotlin
import com.example.android.playground.todo.presentation.navigation.todoNavigation

NavHost(navController, startDestination = "main") {
    // ... other destinations
    todoNavigation(navController)
}
```

2. **Navigate to Todo**: Use navigation actions to navigate to todo screens:
```kotlin
import com.example.android.playground.todo.presentation.navigation.TodoNavigationActions

// Navigate to todo list
TodoNavigationActions.navigateToTodoList(navController)

// Navigate to specific todo
TodoNavigationActions.navigateToTodoDetail(navController, todoId = 123)

// Navigate to new todo
TodoNavigationActions.navigateToTodoDetail(navController, todoId = 0)
```

### Navigation Routes
- `todo_list` - Todo list screen
- `todo_detail/{todoId}` - Todo detail screen (0 for new todo)

## Dependencies

Required dependencies are already configured in the module's `build.gradle.kts`:

- **Jetpack Compose** - UI framework
- **Room** - Local database
- **Hilt** - Dependency injection
- **Navigation Compose** - Screen navigation
- **Lifecycle ViewModel** - State management

## Database Schema

### Todo Table
```sql
CREATE TABLE todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
)
```

## State Management

The module uses StateFlow for reactive state management:
- ViewModels expose StateFlow for UI state
- Repository returns Flow for data streams
- UI screens collect state using `collectAsStateWithLifecycle`

## Error Handling

- Repository operations use try-catch for database errors
- UI displays error messages in Cards
- Loading states shown during async operations
- Validation for empty todo titles

## Future Enhancements

Potential improvements that could be added:
- Todo categories/tags
- Due dates and reminders
- Priority levels
- Data export/import
- Dark mode support
- Animations and transitions
- Offline support with sync
- Search filters and sorting options
