package com.example.android_engineer_technical_assignment.repository

import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.data.MovieDao
import com.example.android_engineer_technical_assignment.data.MovieService
import com.example.android_engineer_technical_assignment.data.Constant
import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interface to define the repository pattern actions
 */
interface MovieRepository {
    // API Movies
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun refreshMovies(page: Int)

    // Favorite Movies
    fun getAllFavorites(): Flow<List<FavoriteMovie>>
    suspend fun insertFavorite(movie: FavoriteMovie)
    suspend fun deleteFavorite(movie: FavoriteMovie)
}


/**
 * Implementation of the repository pattern
 * @param MovieService apiService, instance of the API
 * @param MovieDao moviedao, instance of the database
 */

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieService, 
    private val moviedao: MovieDao
) : MovieRepository {

    /**
     * Get all the movies from the database
     * @return [Flow], array that changes when a change is made in the favourite movie
     */
    override suspend fun getMovies(): Flow<List<Movie>> = moviedao.getAllMovies()

    /**
     * Refresh the movies to the database
     * @param page, paging of the API
     */
    override suspend fun refreshMovies(page: Int){
        try {
            // Explicitly passing the API KEY to ensure it is sent correctly
            val response = apiService.getMovies(apiKey = Constant.API_KEY, page = page)
            val allMovies = response.results

            Log.d("MovieRepository", "Fetched ${allMovies.size} movies from API (page $page)")

            // Delete all the old movies to start from 0 only on first page
            if (page == 1) {
                moviedao.deleteAllMovies()
            }

            moviedao.insertAll(allMovies)
        } catch (e: Exception){
            Log.e("MovieRepository", "Error fetching movies from API", e)
            throw e
        }
    }

    // Favorite Movies implementation
    override fun getAllFavorites(): Flow<List<FavoriteMovie>> = moviedao.getAllFavorites()

    override suspend fun insertFavorite(movie: FavoriteMovie) {
        moviedao.insertFavorite(movie)
    }

    override suspend fun deleteFavorite(movie: FavoriteMovie) {
        moviedao.deleteFavorite(movie)
    }
}
