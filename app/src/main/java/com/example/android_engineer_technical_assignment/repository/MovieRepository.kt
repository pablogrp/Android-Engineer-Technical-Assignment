package com.example.android_engineer_technical_assignment.repository

import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.data.MovieDao
import com.example.android_engineer_technical_assignment.data.MovieService
import com.example.android_engineer_technical_assignment.data.Constant
import com.example.android_engineer_technical_assignment.data.MovieDto
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
    suspend fun getMovieByTitle(title: String): Movie?
    suspend fun searchMovieRemote(title: String): Movie?

    // Favorite Movies
    fun getAllFavorites(): Flow<List<FavoriteMovie>>
    suspend fun insertFavorite(movie: FavoriteMovie)
    suspend fun deleteFavorite(movie: FavoriteMovie)
    suspend fun getFavoriteByTitle(title: String): FavoriteMovie?
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
            val response = apiService.getMovies(apiKey = Constant.API_KEY, page = page)
            
            // Map DTOs from network to Room Entities
            val allMovies = response.results.map { dto ->
                Movie(
                    title = dto.title,
                    posterpath = dto.posterPath,
                    overview = dto.overview
                )
            }

            Log.d("MovieRepository", "Fetched ${allMovies.size} movies from API (page $page)")

            if (page == 1) {
                moviedao.deleteAllMovies()
            }

            moviedao.insertAll(allMovies)
        } catch (e: Exception){
            Log.e("MovieRepository", "Error fetching movies from API", e)
            throw e
        }
    }

    override suspend fun getMovieByTitle(title: String): Movie? = moviedao.getMovieByTitle(title)

    override suspend fun searchMovieRemote(title: String): Movie? {
        return try {
            val response = apiService.searchMovies(query = title)
            val dto = response.results.firstOrNull()
            
            // Map DTO to Room Entity
            dto?.let {
                Movie(
                    title = it.title,
                    posterpath = it.posterPath,
                    overview = it.overview
                )
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error searching movie by title: $title", e)
            null
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

    override suspend fun getFavoriteByTitle(title: String): FavoriteMovie? = moviedao.getFavoriteByTitle(title)
}
