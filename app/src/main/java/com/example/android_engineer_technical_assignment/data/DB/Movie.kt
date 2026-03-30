package com.example.android_engineer_technical_assignment.data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
* Data class for Room Database
*/
@Entity(tableName = "movies")
data class Movie (
    @PrimaryKey
    val title: String,
    val posterpath: String?,
    val overview: String?
)
