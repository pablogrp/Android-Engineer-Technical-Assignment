package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_engineer_technical_assignment.ui.components.MovieItem
import com.example.android_engineer_technical_assignment.viewmodel.MovieUiState
import com.example.android_engineer_technical_assignment.viewmodel.MovieViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * UI screen that shows the movies brought
 *
 * @param NavHostController navController, controller that allows to go to the Detail or the Favourite screen
 * @param Modifier modifier
 * @param MovieViewModel view, Manage the API call and the state
 *
 */
@Composable
fun MovieScreen(navController: NavHostController, modifier: Modifier = Modifier, viewModel: MovieViewModel = viewModel()) {

    // Obtain the current state (loading, error or success)
    val state = viewModel.uiState

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {

            // Favourite botton
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                        Text(text = "No movie data available.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(movieList) { currentMovie ->
                                MovieItem(
                                    movie = currentMovie,
                                    onSeeMoreClick = {
                                        val cleanPoster = currentMovie.posterpath?.removePrefix("/") ?: "null"
                                        val encodedOverview = URLEncoder.encode(
                                            currentMovie.overview ?: "No description",
                                            StandardCharsets.UTF_8.toString()
                                        )

                                        navController.navigate("more_info/${currentMovie.title}/$cleanPoster/$encodedOverview")
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