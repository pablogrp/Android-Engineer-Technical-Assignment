package com.example.android_engineer_technical_assignment.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Actions that can be done in the database
 */
@Dao
interface MovieDao {

    /**
     * Get all the movies marked as favourites
     * @return [Flow], reactive array that changes whenever a change is made in the favourite movie
     */
    @Query("SELECT * FROM favorite_movies") // Select al the movies
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