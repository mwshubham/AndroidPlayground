package com.example.android.systemdesign.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.android.systemdesign.domain.model.SystemDesignTopicId
import com.example.android.systemdesign.presentation.components.SystemDesignTopicCard
import com.example.android.systemdesign.ui.theme.AndroidSystemDesignTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onTopicClick: (SystemDesignTopicId) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Android System Design",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = "Explore System Design Topics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        (state.topics).forEach { topic ->
                            SystemDesignTopicCard(
                                topic = topic,
                                onClick = { onTopicClick(topic.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun MainScreenPreview() {
    AndroidSystemDesignTheme(darkTheme = false) {
        MainScreen()
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenDarkPreview() {
    AndroidSystemDesignTheme(darkTheme = true) {
        MainScreen()
    }
}
