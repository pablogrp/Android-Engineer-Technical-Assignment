package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.ui.components.MovieItem
import com.example.android_engineer_technical_assignment.viewmodel.FavoriteViewModel
import com.example.android_engineer_technical_assignment.viewmodel.MovieUiState
import com.example.android_engineer_technical_assignment.viewmodel.MovieViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * UI screen that shows the movies brought with Infinity Scrolling and Favorite toggle
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel = viewModel(),
    favoriteViewModel: FavoriteViewModel = viewModel()
) {

    val state = movieViewModel.uiState
    val searchQuery = movieViewModel.searchQuery
    val favorites by favoriteViewModel.favoriteMovies.collectAsState()
    val listState = rememberLazyListState()

    val isAtEnd = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.lastOrNull()
                (lastVisibleItem?.index ?: 0) >= (layoutInfo.totalItemsCount - 5)
            }
        }
    }

    LaunchedEffect(isAtEnd.value) {
        if (isAtEnd.value && searchQuery.isEmpty()) {
            movieViewModel.loadNextPage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("favorites") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = Color.Red
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "See my favourites"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { movieViewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(text = "Search by name...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { movieViewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    is MovieUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is MovieUiState.Error -> {
                        Text(text = "Error: ${state.errormesage}")
                    }
                    is MovieUiState.Success -> {
                        val movieList = state.movies
                        if (movieList.isEmpty()) {
                            Text(
                                text = if (searchQuery.isEmpty()) "No movie data available."
                                else "No results found for '$searchQuery'"
                            )
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                itemsIndexed(movieList) { index, currentMovie ->
                                    val isFav = favorites.any { it.title == currentMovie.title }
                                    
                                    MovieItem(
                                        movie = currentMovie,
                                        isFavorite = isFav,
                                        onSeeMoreClick = {
                                            val cleanPoster = currentMovie.posterpath?.removePrefix("/") ?: "null"
                                            val encodedOverview = URLEncoder.encode(
                                                currentMovie.overview ?: "No description",
                                                StandardCharsets.UTF_8.toString()
                                            )
                                            navController.navigate("more_info/${currentMovie.title}/$cleanPoster/$encodedOverview")
                                        },
                                        onFavoriteClick = {
                                            val favMovie = FavoriteMovie(
                                                title = currentMovie.title ?: "",
                                                posterPath = currentMovie.posterpath ?: "",
                                                overview = currentMovie.overview ?: ""
                                            )
                                            favoriteViewModel.toggleFavorite(favMovie)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
