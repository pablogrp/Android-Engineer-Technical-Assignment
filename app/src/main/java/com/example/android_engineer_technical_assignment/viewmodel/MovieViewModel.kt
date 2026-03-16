package com.example.android_engineer_technical_assignment.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.data.RetrofitClient
import kotlinx.coroutines.launch

// Represent the different states
sealed class MovieUiState {
    object Loading : MovieUiState() // Waiting until getting the data
    data class Success(val movies: List<Movie>) : MovieUiState() // Collected all the data
    data class Error(val errormesage: String) : MovieUiState()
}

/**
 * ViewModel that manages the obtainment of the movies from the API
 * and handles the search filtering logic.
 */
class MovieViewModel : ViewModel() {

    // Main UI state that the Screen observes
    var uiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    // Holds the current text entered in the search bar
    var searchQuery by mutableStateOf("")
        private set

    // Internal cache to keep the original list for filtering
    private var allMovies: List<Movie> = emptyList()

    init {
        fetchMovies()
    }

    /**
     * Fetch the popular movies from the remote API
     */
    fun fetchMovies() {
        viewModelScope.launch {
            uiState = MovieUiState.Loading
            try {
                val response = RetrofitClient.apiService.getMovies()
                val movies = response.results

                // Store the original result in our cache
                allMovies = movies

                Log.d("MovieViewModel", "Fetched ${movies.size} movies successfully")
                uiState = MovieUiState.Success(movies)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movies", e)
                uiState = MovieUiState.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    /**
     * Updates the search query and filters the list in real-time.
     * * @param newQuery The text to filter movie titles.
     */
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery

        // Filter from the cached 'allMovies' to ensure we don't lose data
        if (allMovies.isNotEmpty()) {
            val filteredList = if (newQuery.isEmpty()) {
                allMovies
            } else {
                allMovies.filter { movie ->
                    // Null-safe check for the title using safe call and comparison
                    movie.title?.contains(newQuery, ignoreCase = true) == true
                }
            }
            // Update the UI state with the filtered result
            uiState = MovieUiState.Success(filteredList)
        }
    }
}