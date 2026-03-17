package com.example.android_engineer_technical_assignment.repository

import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.data.MovieDao
import com.example.android_engineer_technical_assignment.data.MovieService
import android.util.Log
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun refreshMovies(page: Int)
}

class MovieRepositoryImpl(private val apiService: MovieService, private val moviedao: MovieDao) : MovieRepository {

    override suspend fun getMovies(): Flow<List<Movie>> = moviedao.getAllMovies()

    override suspend fun refreshMovies(page: Int){
        try {
            val response = apiService.getMovies(page = page)
            val allMovies = response.results

            if (page == 1) {
                moviedao.deleteAllMovies()
            }

            moviedao.insertAll(allMovies)
        } catch (e: Exception){
            Log.e("MovieRepository", "Error fetching movies", e)
        }
    }
}
