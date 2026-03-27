package com.example.android_engineer_technical_assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel that manages favourite movies
 * @property repository Repository to realize action in the DataBase
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    /**
     * Toggle the state of the movie (add or remove from the list)
     * @param FavoriteMovie movie, movie to include or delete from the Database
     */
    fun toggleFavorite(movie: FavoriteMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = favoriteMovies.value.any { it.title == movie.title }
            if (isFav) {
                repository.deleteFavorite(movie)
            } else {
                repository.insertFavorite(movie)
            }
        }
    }
}
