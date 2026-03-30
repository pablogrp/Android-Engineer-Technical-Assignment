package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.ui.components.FavoriteConfirmationDialog
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import com.example.android_engineer_technical_assignment.viewmodel.DetailViewModel

/**
 * UI screen that shows the details of the movies and allow the "favourite movie" management
 */

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    DetailScreenContent(
        title = viewModel.title,
        posterPath = viewModel.posterPath,
        overview = viewModel.overview,
        isFavorite = viewModel.isFavorite,
        isLoading = viewModel.isLoading,
        onBack = onBack,
        onFavoriteClick = { viewModel.toggleFavorite() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    title: String,
    posterPath: String,
    overview: String,
    isFavorite: Boolean,
    isLoading: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    FavoriteConfirmationDialog(
        show = showDialog,
        onConfirm = {
            onFavoriteClick()
            showDialog = false
        },
        onDismiss = { showDialog = false }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                            contentDescription = "Return"
                        )
                    }
                },
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    if (!isLoading) {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Add to Favourites",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                if (posterPath.isNotEmpty() && posterPath != "null") {
                    val imageUrl = if (posterPath.startsWith("http")) posterPath
                    else "https://image.tmdb.org/t/p/w500/$posterPath"
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Poster",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = overview.ifEmpty { "No overview available." },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Android_Engineer_Technical_AssignmentTheme {
        DetailScreenContent(
            title = "Example movie title",
            posterPath = "",
            overview = "Example movie overview",
            isFavorite = false,
            isLoading = false,
            onBack = {},
            onFavoriteClick = {}
        )
    }
}
