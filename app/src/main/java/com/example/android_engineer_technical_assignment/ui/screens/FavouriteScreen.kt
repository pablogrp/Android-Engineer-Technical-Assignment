package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import com.example.android_engineer_technical_assignment.viewmodel.FavoriteViewModel


/**
 * UI screen that shows the favourite movies marked locally
 *
 * @param FavoriteViewModel viewmodel,
 * @param Unit onMovieClick, Callback to navigate to the detail screen
 * @param Unit onBack, Callback to return to the main screen
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: FavoriteViewModel, onMovieClick: (FavoriteMovie) -> Unit, onBack: () -> Unit) {

    val favorites by viewModel.favoriteMovies.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourite Movies") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                            contentDescription = "Return"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No movie has been added to the list.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp), // Add padding around the gird
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Add horizontal spacing between items
                verticalArrangement = Arrangement.spacedBy(16.dp) // Add vertical spacing between items
            ) {
                items(favorites) { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMovieClick(movie) },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 9.dp)
                    ) {
                        Column {
                            val imageUrl = if (movie.posterPath.startsWith("http")) movie.posterPath
                            else "https://image.tmdb.org/t/p/w500/${movie.posterPath}"

                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2f / 3f)
                                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Different previews to see how the grid layout works on other sizes
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, device = Devices.PHONE, name = "1. Phone / Fold Closed")
@Preview(showBackground = true, device = Devices.FOLDABLE, name = "2. Fold Open")
@Preview(showBackground = true, device = Devices.TABLET, name = "3. Tablet")
@Composable
fun FavoritesScreenPreview() {
    // To test
    val mockFavorites = listOf(
        FavoriteMovie(title = "Movie 1", overview = "Example overview 1", posterPath = ""),
        FavoriteMovie(title = "Example mov0e 2", overview = "Example overview 2", posterPath = ""),
        FavoriteMovie(title = "Movie 3", overview = "Example overview 3", posterPath = ""),
        FavoriteMovie(title = "Example movie 4 with ellipsis", overview = "Example overview 4", posterPath = ""),
        FavoriteMovie(title = "Movie 1", overview = "Example overview 1", posterPath = ""),
        FavoriteMovie(title = "Example mov0e 2", overview = "Example overview 2", posterPath = ""),
        FavoriteMovie(title = "Movie 3", overview = "Example overview 3", posterPath = ""),
        FavoriteMovie(title = "Example movie 4 with ellipsis", overview = "Example overview 4", posterPath = ""),
        FavoriteMovie(title = "Movie 1", overview = "Example overview 1", posterPath = ""),
        FavoriteMovie(title = "Example mov0e 2", overview = "Example overview 2", posterPath = ""),
        FavoriteMovie(title = "Movie 3", overview = "Example overview 3", posterPath = ""),
        FavoriteMovie(title = "Example movie 4 with ellipsis", overview = "Example overview 4", posterPath = ""),
        FavoriteMovie(title = "Movie 1", overview = "Example overview 1", posterPath = ""),
        FavoriteMovie(title = "Example mov0e 2", overview = "Example overview 2", posterPath = ""),
        FavoriteMovie(title = "Movie 3", overview = "Example overview 3", posterPath = ""),
        FavoriteMovie(title = "Example movie 4 with ellipsis", overview = "Example overview 4", posterPath = "")
    )

    //
    Android_Engineer_Technical_AssignmentTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Favourite Movies") }) }
            ) { padding ->
                LazyVerticalGrid( // Lazy Vertical grid to use the full screen width depending on the device
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mockFavorites) { movie ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(2f / 3f)
                                        .background(Color.LightGray)
                                )
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
