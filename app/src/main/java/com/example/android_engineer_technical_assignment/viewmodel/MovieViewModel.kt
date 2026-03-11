package com.example.android_engineer_technical_assignment.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_engineer_technical_assignment.data.Movie
import com.example.android_engineer_technical_assignment.data.RetrofitClient
import kotlinx.coroutines.launch

sealed class MovieUiState{
    object Loading : MovieUiState() // Waiting until getting the data
    data class Success(val movies: List<Movie>) : MovieUiState() // Collected all the data
    data class Error(val errormesage: String): MovieUiState()
}

class MovieViewModel: ViewModel(){
    var uiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set
    init {
        Log.d("MovieViewModel", "ViewModel its running")
        fetchMovies()
    }
    fun fetchMovies(){
        viewModelScope.launch {
            uiState = MovieUiState.Loading
            try {
                val response = RetrofitClient.apiService.getMovies()
                val movies = response.results
                Log.d("MovieViewModel", "Fetched ${movies.size} movies successfully")
                uiState = MovieUiState.Success(movies)
            } catch (e: Exception){
                Log.e("MovieViewModel0", "Error",e)
                uiState = MovieUiState.Error(e.localizedMessage ?: "Error")
            }
        }
    }
}