package com.example.android_engineer_technical_assignment.data

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Data class which represents the local database
 * @property String title, title of the movie (PrimaryKey)
 * @property String posterPath, posterPath URL
 * @property String overview, small description of the movie
 */
@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false) // Only one Database
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}