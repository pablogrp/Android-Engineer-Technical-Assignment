package com.example.android_engineer_technical_assignment.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _overview = MutableStateFlow("")
    val overview: StateFlow<String> = _overview.asStateFlow()

    private val _posterPath = MutableStateFlow("")
    val posterPath: StateFlow<String> = _posterPath.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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
                _isFavorite.value = list.any { it.title == title }
            }
        }
    }

    /**
     * Load the movie info
     */
    private fun loadDetails() {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Check local data
            val movie = repository.getMovieByTitle(title)
            if (movie != null) {
                _overview.value = movie.overview ?: ""
                _posterPath.value = movie.posterpath?.removePrefix("/") ?: ""
            }

            // 2. If it is empty, check favorites or API
            if (_overview.value.isEmpty()) {
                val fav = repository.getFavoriteByTitle(title)
                if (fav != null) {
                    _overview.value = fav.overview
                    _posterPath.value = fav.posterPath.removePrefix("/")
                } else {
                    // Last option: search in the API
                    val remote = repository.searchMovieRemote(title)
                    if (remote != null) {
                        _overview.value = remote.overview ?: ""
                        _posterPath.value = remote.posterpath?.removePrefix("/") ?: ""
                    }
                }
            }
            _isLoading.value = false
        }
    }

    /**
     * Save or delete the movie from favorites
     */
    fun toggleFavorite() {
        viewModelScope.launch {
            val movie = FavoriteMovie(title, _posterPath.value, _overview.value)
            if (_isFavorite.value) {
                repository.deleteFavorite(movie)
            } else {
                repository.insertFavorite(movie)
            }
        }
    }
}
