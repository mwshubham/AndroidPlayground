---
name: mvi-unit-test-generator
description: "Generate unit tests for MVI ViewModels in AndroidPlayground using Turbine, MockK, MainDispatcherRule, and runTest. Covers state assertions, side-effect emissions, and use-case verification."
metadata:
  author: AndroidPlayground
  keywords:
    - testing
    - unit-test
    - turbine
    - mockk
    - mvi
    - viewmodel
---

## Overview

Use this skill to generate idiomatic unit tests for any `{Feature}ViewModel` in AndroidPlayground.
Tests reside in `feature/{feature}/impl/src/test/kotlin/com/example/android/playground/{feature}/presentation/viewmodel/{Feature}ViewModelTest.kt`.

---

## Canonical Test Template

```kotlin
package com.example.android.playground.{feature}.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.{feature}.domain.usecase.{Action}UseCase
import com.example.android.playground.{feature}.presentation.intent.{Feature}Intent
import com.example.android.playground.{feature}.presentation.sideeffect.{Feature}SideEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class {Feature}ViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val {action}UseCase: {Action}UseCase = mockk()

    private fun createViewModel(): {Feature}ViewModel {
        return {Feature}ViewModel({action}UseCase)
    }

    @Test
    fun `initial state has default values and isLoading is false`() = runTest {
        val viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handling intent updates state correctly`() = runTest {
        val viewModel = createViewModel()

        viewModel.handleIntent({Feature}Intent.OnScreenStarted)

        viewModel.state.test {
            val state = awaitItem()
            // Assert state changes
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handling navigation intent emits NavigateBack side effect`() = runTest {
        val viewModel = createViewModel()

        viewModel.sideEffect.test {
            viewModel.handleIntent({Feature}Intent.OnNavigateBackClicked)
            val effect = awaitItem()
            assertEquals({Feature}SideEffect.NavigateBack, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

---

## Testing Guidelines

1. **Dispatcher Rule**: Always use `@get:Rule val mainDispatcherRule = MainDispatcherRule()`. Do not set dispatchers manually.
2. **Coroutines**: Wrap test functions in `runTest { ... }`.
3. **Turbine for Flows**:
   - For state: `viewModel.state.test { ... val state = awaitItem(); ... cancelAndIgnoreRemainingEvents() }`.
   - For side effects: Start collecting `viewModel.sideEffect.test` **before** dispatching the intent that produces the side-effect to avoid missing buffered events.
4. **Mocking**: Use MockK (`mockk()`, `coEvery { ... } returns ...`, `coVerify { ... }`).
5. **Naming**: Use descriptive backtick names explaining intent and expected outcome:
   ```kotlin
   @Test
   fun `intent Foo produces Bar state and calls UseCase`() = runTest { ... }
   ```
6. **Execution**:
   Run `./gradlew :feature:{feature}:impl:test` to verify tests pass.
