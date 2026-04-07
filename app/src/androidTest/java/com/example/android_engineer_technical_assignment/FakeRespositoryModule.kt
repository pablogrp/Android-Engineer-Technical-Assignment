package com.example.android_engineer_technical_assignment.di

import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

// FakeRepository
class FakeRepository : MovieRepository {
    private val movie1 = Movie("Avatar", "/avatar.jpg", "Overview 1")
    private val movie2 = Movie("Inception", "/inception.jpg", "Overview 2")
    private val movie3 = Movie("Titanic", "/titanic.jpg", "Overview 3")
    
    private val _movies = MutableStateFlow(listOf(movie1, movie2, movie3))
    private val _favorites = MutableStateFlow<List<FavoriteMovie>>(emptyList())

    override suspend fun getMovies(): Flow<List<Movie>> = _movies
    override suspend fun refreshMovies(page: Int) {}
    override suspend fun getMovieByTitle(title: String): Movie? =
        _movies.value.find { it.title == title }

    override suspend fun searchMovieRemote(title: String): Movie? =
        _movies.value.find { it.title == title }

    override fun getAllFavorites(): Flow<List<FavoriteMovie>> = _favorites

    override suspend fun insertFavorite(movie: FavoriteMovie) {
        _favorites.value = _favorites.value + movie
    }

    override suspend fun deleteFavorite(movie: FavoriteMovie) {
        _favorites.value = _favorites.value.filter { it.title != movie.title }
    }

    override suspend fun getFavoriteByTitle(title: String): FavoriteMovie? =
        _favorites.value.find { it.title == title }
}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [com.example.android_engineer_technical_assignment.hilt.MovieModule::class]
)
object FakeRepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(): MovieRepository = FakeRepository()
}
