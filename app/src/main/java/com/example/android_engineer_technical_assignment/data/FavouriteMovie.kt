package com.example.android_engineer_technical_assignment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey
    val title: String,
    val posterPath: String,
    val overview: String
)