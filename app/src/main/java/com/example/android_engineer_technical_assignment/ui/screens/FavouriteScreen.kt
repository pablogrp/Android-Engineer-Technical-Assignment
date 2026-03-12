package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.data.FavoriteMovie
import com.example.android_engineer_technical_assignment.viewmodel.FavoriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoriteViewModel,
    onMovieClick: (FavoriteMovie) -> Unit,
    onBack: () -> Unit
) {
    val favorites by viewModel.favoriteMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourite Movies") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                    text = "Aún no has guardado ninguna película.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Lista de favoritos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(favorites) { movie ->
                    ListItem(
                        modifier = Modifier.clickable { onMovieClick(movie) },
                        headlineContent = { Text(movie.title) },
                        supportingContent = {
                            Text(
                                text = movie.overview,
                                maxLines = 2,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingContent = {
                            val imageUrl = if (movie.posterPath.startsWith("http")) movie.posterPath
                            else "https://image.tmdb.org/t/p/w200/${movie.posterPath}"

                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}