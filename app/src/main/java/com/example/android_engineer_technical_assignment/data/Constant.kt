package com.example.android_engineer_technical_assignment.data

import com.example.android_engineer_technical_assignment.BuildConfig

/**
 * Main constants of the api
 * @property String BASE_URL, main url to the API
 * @property String API_KEY, key to access the API
 */

object Constant {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    val API_KEY = BuildConfig.TMDB_API_KEY
}
