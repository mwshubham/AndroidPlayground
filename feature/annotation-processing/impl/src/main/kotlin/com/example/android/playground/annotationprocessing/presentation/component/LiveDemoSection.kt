package com.example.android.playground.annotationprocessing.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.android.playground.annotationprocessing.demo.SamplePerson
import com.example.android.playground.annotationprocessing.demo.SampleProduct
import com.example.android.playground.annotationprocessing.demo.SampleUser
import com.example.android.playground.annotationprocessing.demo.generatedToString
import com.example.android.playground.annotationprocessing.demo.toJson
import com.example.android.playground.annotationprocessing.demo.validate
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

private val demoTabs = listOf("toString", "toJson", "validate")

@Composable
internal fun LiveDemoSection(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Live demos",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "Edit the fields in each tab to see generated output update in real time.",
            style = MaterialTheme.typography.bodyMedium,
        )
        TabRow(selectedTabIndex = selectedTab) {
            demoTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                )
            }
        }
        when (selectedTab) {
            0 -> AutoToStringDemoSection()
            1 -> AutoJsonDemoSection()
            else -> ValidateDemoSection()
        }
    }
}

// ---- Section A ----

@Composable
private fun AutoToStringDemoSection() {
    var nameInput by remember { mutableStateOf("Alice") }
    var ageInput by remember { mutableStateOf("30") }
    var emailInput by remember { mutableStateOf("alice@example.com") }

    val output by remember(nameInput, ageInput, emailInput) {
        derivedStateOf {
            SamplePerson(
                name = nameInput,
                age = ageInput.toIntOrNull() ?: 0,
                email = emailInput,
            ).generatedToString()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "A) Interactive @AutoToString",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = ageInput,
            onValueChange = { input -> ageInput = input.filter { it.isDigit() } },
            label = { Text("Age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        CodeSnippetCard(
            label = "1) Annotation",
            code =
                """
                @Target(AnnotationTarget.CLASS)
                @Retention(AnnotationRetention.SOURCE)
                annotation class AutoToString
                """.trimIndent(),
        )
        CodeSnippetCard(
            label = "2) Annotated class",
            code =
                """
                @AutoToString
                data class SamplePerson(
                    val name: String,
                    val age: Int,
                    val email: String,
                )
                """.trimIndent(),
        )
        CodeSnippetCard(
            label = "3) Generated file",
            code =
                """
                // AUTO-GENERATED by AutoToStringProcessor - do not edit

                fun SamplePerson.generatedToString(): String {
                    return "SamplePerson(name=${'$'}name, age=${'$'}age, email=${'$'}email)"
                }
                """.trimIndent(),
        )
        DemoOutputBox(label = "4) Live output", text = output)
    }
}

// ---- Section B ----

@Composable
private fun AutoJsonDemoSection() {
    var nameInput by remember { mutableStateOf("Laptop") }
    var priceInput by remember { mutableStateOf("999.99") }
    var inStockInput by remember { mutableStateOf(true) }

    val output by remember(nameInput, priceInput, inStockInput) {
        derivedStateOf {
            SampleProduct(
                name = nameInput,
                price = priceInput.toDoubleOrNull() ?: 0.0,
                inStock = inStockInput,
            ).toJson()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "B) Interactive @AutoJson",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = priceInput,
            onValueChange = { input -> priceInput = input.filter { it.isDigit() || it == '.' } },
            label = { Text("Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "In Stock", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = inStockInput, onCheckedChange = { inStockInput = it })
        }
        CodeSnippetCard(
            label = "1) Annotation",
            code =
                """
                @Target(AnnotationTarget.CLASS)
                @Retention(AnnotationRetention.SOURCE)
                annotation class AutoJson
                """.trimIndent(),
        )
        CodeSnippetCard(
            label = "2) Annotated class",
            code =
                """
                @AutoJson
                data class SampleProduct(
                    val name: String,
                    val price: Double,
                    val inStock: Boolean,
                )
                """.trimIndent(),
        )
        CodeSnippetCard(
            label = "3) Generated file",
            code =
                """
                // AUTO-GENERATED by AutoJsonProcessor - do not edit

                fun SampleProduct.toJson(): String =
                    "{\"name\":\"${'$'}name\",\"price\":${'$'}price,\"inStock\":${'$'}inStock}"
                """.trimIndent(),
        )
        DemoOutputBox(label = "4) Live output", text = output)
    }
}

// ---- Section C ----

@Composable
private fun ValidateDemoSection() {
    var usernameInput by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("15") }
    var emailInput by remember { mutableStateOf("") }

    val errors by remember(usernameInput, ageInput, emailInput) {
        derivedStateOf {
            SampleUser(
                username = usernameInput,
                age = ageInput.toIntOrNull() ?: 0,
                email = emailInput,
            ).validate()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "C) Interactive @Validate",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = ageInput,
            onValueChange = { input -> ageInput = input.filter { it.isDigit() } },
            label = { Text("Age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        CodeSnippetCard(
            label = "1) Annotations",
            code =
                """
                annotation class Validate
                annotation class NotBlank
                annotation class MinValue(val min: Int)
                """.trimIndent(),
        )
        CodeSnippetCard(
            label = "2) Annotated class",
            code =
                """
                @Validate
                data class SampleUser(
                    @NotBlank val username: String,
                    @MinValue(18) val age: Int,
                    @NotBlank val email: String,
                )
                """.trimIndent(),
        )
        CodeSnippetCard(
            label = "3) Generated file",
            code =
                """
                // AUTO-GENERATED by ValidateProcessor - do not edit

                fun SampleUser.validate(): List<String> {
                    val errors = mutableListOf<String>()
                    if (username.isBlank()) errors.add("username must not be blank")
                    if (age < 18) errors.add("age must be >= 18")
                    if (email.isBlank()) errors.add("email must not be blank")
                    return errors
                }
                """.trimIndent(),
        )
        DemoOutputBox(
            label = "4) Live validation output",
            text = errors.joinToString("\n").ifEmpty { "\u2713 No errors \u2014 all fields are valid" },
        )
    }
}

// ---- Shared helpers ----

@Composable
private fun DemoOutputBox(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun LiveDemoSectionPreview() {
    PreviewContainer {
        LiveDemoSection()
    }
}
