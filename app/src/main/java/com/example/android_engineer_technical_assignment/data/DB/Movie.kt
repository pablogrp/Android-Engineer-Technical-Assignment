package com.example.android_engineer_technical_assignment.data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
* Data class with all the properties for the movie class
* @property title
* @property posterpath, url with the movie poster.
* @property overview
*/
@Entity(tableName = "movies")
data class Movie (
    @PrimaryKey
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterpath: String?,
    @SerializedName("overview") val overview: String?
)