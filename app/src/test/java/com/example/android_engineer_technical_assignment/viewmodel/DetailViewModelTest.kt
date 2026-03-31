package com.example.android_engineer_technical_assignment.viewmodel;

import androidx.annotation.OptIn;
import androidx.lifecycle.SavedStateHandle
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie

import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.collections.emptyList


@OptIn(ExperimentalCoroutinesApi::class)
public class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private val repository: MovieRepository = mock()
    private val testDispatcher = StandardTestDispatcher()


    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown(){
        Dispatchers.resetMain()
    }

    // Test to check the title, the posterPath and the overview
    @Test
    fun `tittle, posterPath and overview are set correctly`() = runTest{
        val movieTitle = "Example1"
        val movieOverview = ""
        val moviePoster = ""

        val savedStateHandle = SavedStateHandle(mapOf("movieTitle" to movieTitle))
        val movie = Movie(title = movieTitle, posterpath = moviePoster, overview = movieOverview)

        whenever(repository.getAllFavorites()).thenReturn(flowOf(emptyList()))
        whenever(repository.getMovieByTitle(movieTitle)).thenReturn(movie)

        viewModel = DetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        assertEquals(movieTitle, viewModel.title)
        assertEquals(moviePoster, viewModel.posterPath)
    }

    // Test to check if the movie is favorite
    @Test
    fun `isFavorite is set correctly`() = runTest{
        val movieTitle = "Example1"
        val movieOverview = ""
        val moviePoster = ""

        val savedStateHandle = SavedStateHandle(mapOf("movieTitle" to movieTitle))
        val movie = FavoriteMovie(title = movieTitle, posterPath = moviePoster, overview = movieOverview)

        whenever(repository.getAllFavorites()).thenReturn(flowOf(listOf(movie)))
        whenever(repository.getMovieByTitle(movieTitle)).thenReturn(null)

        viewModel = DetailViewModel(repository, savedStateHandle)
        advanceUntilIdle()
        assertTrue { viewModel.isFavorite }
    }
}
