package com.example.android_engineer_technical_assignment.repository

import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.data.MovieDao
import com.example.android_engineer_technical_assignment.data.MovieService
import android.util.Log
import kotlinx.coroutines.flow.Flow

/**
 * Interface to define the repository pattern actions
 */
interface MovieRepository {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun refreshMovies(page: Int)
}


/**
 * Implementation of the repository pattern
 * @param MovieService apiService, instance of the API
 * @param MovieDao moviedao, instance of the database
 */

class MovieRepositoryImpl(private val apiService: MovieService, private val moviedao: MovieDao) : MovieRepository {

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
            val response = apiService.getMovies(page = page)
            val allMovies = response.results

            // Delete all the old movies to start from 0
            if (page == 1) {
                moviedao.deleteAllMovies()
            }

            moviedao.insertAll(allMovies)
        } catch (e: Exception){
            Log.e("MovieRepository", "Error fetching movies", e)
        }
    }
}
