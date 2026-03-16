package com.example.android_engineer_technical_assignment.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.data.DB.Movie


/**
 * Data class which represents the local database
 * @property String title, title of the movie (PrimaryKey)
 * @property String posterPath, posterPath URL
 * @property String overview, small description of the movie
 */
@Database(entities = [FavoriteMovie::class, Movie::class], version = 2, exportSchema = false) // Two databases, one for the movies and one for the favourites

abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}