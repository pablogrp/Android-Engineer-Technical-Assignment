package com.example.android_engineer_technical_assignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class which represents the local database
 * @property String title, title of the movie (PrimaryKey)
 * @property String posterPath, posterPath URL
 * @property String overview, small description of the movie
 */

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey
    val title: String,
    val posterPath: String,
    val overview: String
)