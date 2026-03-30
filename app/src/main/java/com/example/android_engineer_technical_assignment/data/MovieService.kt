package com.example.android_engineer_technical_assignment.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Connection with the API

/**
 * Interface to define the connection routes with the movie API
 */

interface MovieService {

    /**
     * Get request to take the most popular movies
     */
    @GET("movie/top_rated")
    suspend fun getMovies(@Query("api_key") apiKey: String = Constant.API_KEY,
                          @Query("page") page: Int): MovieResponse

    /**
     * Search for movies by title
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = Constant.API_KEY,
        @Query("query") query: String
    ): MovieResponse
}

/**
 * Single instance to create all the resources once
 */

object RetrofitClient {
    val apiService: MovieService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
    }
}
