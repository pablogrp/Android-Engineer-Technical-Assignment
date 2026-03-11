package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.android_engineer_technical_assignment.data.Movie
import com.example.android_engineer_technical_assignment.data.RetrofitClient
import com.example.android_engineer_technical_assignment.ui.components.MovieItem
import com.example.android_engineer_technical_assignment.ui.viewmodel.MovieViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun MovieScreen(navController: NavHostController, modifier: Modifier = Modifier, viewModel: MovieViewModel = viewModel()) {
    val state = viewModel.uiState
    var movieList by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getMovies()
            movieList = response.results
            isLoading = false
        } catch (e: Exception) {
            isError = true
            isLoading = false
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            isError -> Text(text = "Error.")
            movieList.isEmpty() -> Text(text = "No movie data available.")
            else -> {
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

