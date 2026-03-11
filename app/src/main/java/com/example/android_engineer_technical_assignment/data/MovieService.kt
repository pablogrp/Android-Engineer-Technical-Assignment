package com.example.android_engineer_technical_assignment.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun getMovies(@Query("api_key") apiKey: String = Constant.API_KEY): MovieResponse
}

object RetrofitClient {
    val apiService: MovieService by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
    }
}