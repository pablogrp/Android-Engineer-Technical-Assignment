package com.example.android_engineer_technical_assignment.data

import com.example.android_engineer_technical_assignment.data.DB.Movie


/**
 * Data class for the API movies
 * @property List<Movie> Array with the movies brought by the API
 */
data class MovieResponse(
    val results: List<Movie>
)
