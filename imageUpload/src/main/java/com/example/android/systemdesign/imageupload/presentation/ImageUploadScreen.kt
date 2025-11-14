package com.example.android.systemdesign.imageupload.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.android.systemdesign.imageupload.presentation.components.ClearResultButton
import com.example.android.systemdesign.imageupload.presentation.components.CompletionCard
import com.example.android.systemdesign.imageupload.presentation.components.FailureCard
import com.example.android.systemdesign.imageupload.presentation.components.StartUploadButton
import com.example.android.systemdesign.imageupload.presentation.components.SuccessCard

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
    // Handle system back press same as top app bar back arrow
    BackHandler {
        handleIntent(ImageUploadIntent.NavigationBack)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Image Upload App",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        handleIntent(ImageUploadIntent.NavigationBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Upload Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { state.progress },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${state.uploadResults.size}/${state.totalCount} images processed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (state.isUploading) {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Uploading...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

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

@Preview(showBackground = true)
@Composable
fun ImageUploadScreenPreview1() {
    MaterialTheme {
        ImageUploadScreenContent(
            state = ImageUploadState(
                isUploading = false
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ImageUploadScreenPreview2() {
    MaterialTheme {
        ImageUploadScreenContent(
            state = ImageUploadState(
                isUploading = true
            )
        ) {}
    }
}
