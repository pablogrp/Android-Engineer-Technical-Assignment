package com.example.android_engineer_technical_assignment.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface to define the connection routes with the movie API
 */

interface MovieService {

    /**
     * Get request to take the most popular movies
     * @param apiKey, key to access the API
     * @return [MovieResponse], object that contains the array of the movies
     */
    @GET("movie/top_rated")
    suspend fun getMovies(@Query("api_key") apiKey: String = Constant.API_KEY,
                          @Query("page") page: Int):MovieResponse // Get the pages of the api
}

/**
 * Single instance to create all the resources once
 */

object RetrofitClient {
    val apiService: MovieService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL) // Connect to the movie API
            .addConverterFactory(GsonConverterFactory.create()) // Converter from JSON
            .build()
            .create(MovieService::class.java)
    }
}
