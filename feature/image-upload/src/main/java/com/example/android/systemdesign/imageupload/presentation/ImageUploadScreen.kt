package com.example.android.systemdesign.imageupload.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.android.systemdesign.core.ui.components.AppTopAppBar
import com.example.android.systemdesign.core.ui.preview.ComponentPreview
import com.example.android.systemdesign.core.ui.preview.PreviewContainer
import com.example.android.systemdesign.imageupload.presentation.components.ClearResultButton
import com.example.android.systemdesign.imageupload.presentation.components.CompletionCard
import com.example.android.systemdesign.imageupload.presentation.components.FailureCard
import com.example.android.systemdesign.imageupload.presentation.components.StartUploadButton
import com.example.android.systemdesign.imageupload.presentation.components.SuccessCard
import com.example.android.systemdesign.imageupload.presentation.components.UploadProgressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUploadScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ImageUploadViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ImageUploadScreenContent(
        state = state
    ) { intent ->
        when (intent) {
            is ImageUploadIntent.NavigationBack -> onNavigateBack()
            else -> viewModel.handleIntent(intent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageUploadScreenContent(
    state: ImageUploadState,
    handleIntent: (ImageUploadIntent) -> Unit
) {

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Image Upload App",
                onNavigationClick = { handleIntent(ImageUploadIntent.NavigationBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Bulk Image Upload",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Upload 100 dummy images to test the upload service",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Upload Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StartUploadButton(state) {
                    handleIntent(ImageUploadIntent.StartUpload)
                }

                if (!state.isUploading && state.uploadResults.isNotEmpty()) {
                    ClearResultButton {
                        handleIntent(ImageUploadIntent.ClearResults)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Section
            if (state.isUploading || state.uploadResults.isNotEmpty()) {
                UploadProgressCard(state)

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Success and Failure Boxes
            if (state.uploadResults.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Success Box
                    SuccessCard(state)

                    // Failure Box
                    FailureCard(state)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Completion Status
                if (state.isCompleted) {
                    CompletionCard()
                }
            }
        }
    }
}


@ComponentPreview
@Composable
fun ImageUploadScreenIdlePreview() {
    PreviewContainer {
        ImageUploadScreenContent(
            state = ImageUploadState(
                isUploading = false
            )
        ) {}
    }
}

@ComponentPreview
@Composable
fun ImageUploadScreenUploadingPreview() {
    PreviewContainer {
        ImageUploadScreenContent(
            state = ImageUploadState(
                isUploading = true
            )
        ) {}
    }
}
