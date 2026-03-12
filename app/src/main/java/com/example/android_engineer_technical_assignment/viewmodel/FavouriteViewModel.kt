package com.example.android_engineer_technical_assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.FavoriteMovie
import com.example.android_engineer_technical_assignment.data.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val dao: MovieDao) : ViewModel() {

    val favoriteMovies: StateFlow<List<FavoriteMovie>> = dao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleFavorite(movie: FavoriteMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = favoriteMovies.value.any { it.title == movie.title }
            if (isFav) {
                dao.deleteFavorite(movie)
            } else {
                dao.insertFavorite(movie)
            }
        }
    }
}