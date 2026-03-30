package com.example.android_engineer_technical_assignment.data

import com.google.gson.annotations.SerializedName

// Brings all the information from the API


/**
 * Data class for the API response
 */
data class MovieResponse(
    @SerializedName("results") val results: List<MovieDto>
)

/**
 * Data Transfer Object for the API
 */
data class MovieDto(
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String?
)
