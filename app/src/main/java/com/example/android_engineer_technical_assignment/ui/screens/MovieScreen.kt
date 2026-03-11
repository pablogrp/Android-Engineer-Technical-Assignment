package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_engineer_technical_assignment.ui.components.MovieItem
import com.example.android_engineer_technical_assignment.ui.viewmodel.MovieUiState
import com.example.android_engineer_technical_assignment.ui.viewmodel.MovieViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MovieScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel = viewModel()
) {
    val state = viewModel.uiState

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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