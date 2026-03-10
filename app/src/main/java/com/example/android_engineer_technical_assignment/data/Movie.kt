package com.example.android_engineer_technical_assignment.data
import com.google.gson.annotations.SerializedName

/**
* Data class with all the atributes for the movie class
* @property title
* @property posterpath, url with the movie poster.
* @property overview
*/
data class Movie (
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterpath: String?,
    @SerializedName("overview") val overview: String?
)