package com.example.android_engineer_technical_assignment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

/**
 * ViewModel to manage the details of a movie.
 * It searches the information in the database or the API.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get the title from navigation
    val title: String = try {
        val raw = savedStateHandle.get<String>("movieTitle") ?: ""
        URLDecoder.decode(raw, StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        ""
    }

    // State for the UI
    var overview by mutableStateOf("")
        private set

    var posterPath by mutableStateOf("")
        private set

    var isFavorite by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        checkIfFavorite()
        loadDetails()
    }

    /**
     * Check if the movie is in the favorite list
     */
    private fun checkIfFavorite() {
        viewModelScope.launch {
            repository.getAllFavorites().collect { list ->
                isFavorite = list.any { it.title == title }
            }
        }
    }

    /**
     * Load the movie info
     */
    private fun loadDetails() {
        viewModelScope.launch {
            isLoading = true

            // 1. Check local data
            val movie = repository.getMovieByTitle(title)
            if (movie != null) {
                overview = movie.overview ?: ""
                posterPath = movie.posterpath?.removePrefix("/") ?: ""
            }

            // 2. If it is empty, check favorites or API
            if (overview.isEmpty()) {
                val fav = repository.getFavoriteByTitle(title)
                if (fav != null) {
                    overview = fav.overview
                    posterPath = fav.posterPath.removePrefix("/")
                } else {
                    // Last option: search in the API
                    val remote = repository.searchMovieRemote(title)
                    if (remote != null) {
                        overview = remote.overview ?: ""
                        posterPath = remote.posterpath?.removePrefix("/") ?: ""
                    }
                }
            }
            isLoading = false
        }
    }

    /**
     * Save or delete the movie from favorites
     */
    fun toggleFavorite() {
        viewModelScope.launch {
            val movie = FavoriteMovie(title, posterPath, overview)
            if (isFavorite) {
                repository.deleteFavorite(movie)
            } else {
                repository.insertFavorite(movie)
            }
        }
    }
}
