package com.example.android_engineer_technical_assignment.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.data.DB.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Actions that can be done in the database
 */
@Dao
interface MovieDao {

    /**
     * Get all the API movies
     * @return [Flow], reactive array that changes whenever a change is made in the favourite movie
     */
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    /**
     * Get a single movie by title from the main movies table
     */
    @Query("SELECT * FROM movies WHERE title = :title LIMIT 1")
    suspend fun getMovieByTitle(title: String): Movie?

    /**
     * Get a single favorite movie by title
     */
    @Query("SELECT * FROM favorite_movies WHERE title = :title LIMIT 1")
    suspend fun getFavoriteByTitle(title: String): FavoriteMovie?

    /**
     * Save all the movies
     * @param movies, array with the movies
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    /**
     * Clear the cache
     */
    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()


    /**
     * Get all the movies marked as favourites
     * @return [Flow], reactive array that changes whenever a change is made in the favourite movie
     */
    @Query("SELECT * FROM favorite_movies") // Select all the movies
    fun getAllFavorites(): Flow<List<FavoriteMovie>>

    /**
     * Save a movie in the local database
     * @param movie, name of the saved movie
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovie)

    /**
     * Delete a movie from the list
     * @param movie, name of the delete movie
     */
    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovie)

}
