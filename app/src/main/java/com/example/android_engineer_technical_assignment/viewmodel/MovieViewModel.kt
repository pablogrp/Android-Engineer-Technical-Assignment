package com.example.android_engineer_technical_assignment.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Represent the different states
sealed class MovieUiState {
    object Loading : MovieUiState() // Waiting until getting the data
    data class Success(val movies: List<Movie>) : MovieUiState() // Collected all the data
    data class Error(val errormesage: String) : MovieUiState()
}

/**
 * ViewModel that manages the obtainment of the movies from the repository
 * and search filtering logic.
 */
@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    // Main UI state that the Screen observes
    var uiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    // Holds the current text entered in the search bar
    var searchQuery by mutableStateOf("")
        private set

    // Internal cache to keep the original list for filtering
    private var allMovies: List<Movie> = emptyList()
    private var currentPage = 1
    private var isFetchingMore = false

    init {
        observeMovies()
        fetchMovies(currentPage)
    }

    /**
     * Observes the movies flow from the repository.
     * The UI will automatically update whenever the database changes.
     */
    private fun observeMovies() {
        viewModelScope.launch {
            repository.getMovies().collect { moviesFromDb ->
                Log.d("MovieViewModel", "Observed ${moviesFromDb.size} movies from DB")
                allMovies = moviesFromDb
                applyFilter()
            }
        }
    }

    /**
     * Triggers a refresh of the movie data from the remote source.
     */
    fun fetchMovies(page: Int) {
        if (isFetchingMore && page > 1) return

        isFetchingMore = true
        viewModelScope.launch {
            try {
                repository.refreshMovies(page)
                Log.d("MovieViewModel", "Movies refreshed successfully for page $page")
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error refreshing movies", e)
                // If we have no data at all and the refresh fails, show error
                if (allMovies.isEmpty()) {
                    uiState = MovieUiState.Error(e.localizedMessage ?: "Unknown Error")
                }
            } finally {
                isFetchingMore = false
            }
        }
    }

    /**
     * Change the page and fetch the next page of movies.
     */
    fun loadNextPage(){
        if (!isFetchingMore && searchQuery.isEmpty()){
            currentPage++
            fetchMovies(currentPage)
        }
    }

    /**
     * Updates the search query and filters the list in real-time.
     * @param newQuery The text to filter movie titles.
     */
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        applyFilter()
    }

    /**
     * Applies the search filter to the current list of movies.
     */
    private fun applyFilter() {
        val filteredList = if (searchQuery.length < 2) {
            allMovies
        } else {
            allMovies.filter { movie ->
                movie.title.contains(searchQuery, ignoreCase = true)
            }
        }

        // If we have movies, we show them immediately
        if (allMovies.isNotEmpty() || uiState !is MovieUiState.Loading) {
            uiState = MovieUiState.Success(filteredList)
        }
    }
}
