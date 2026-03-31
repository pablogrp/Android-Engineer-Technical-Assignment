package com.example.android_engineer_technical_assignment.viewmodel

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

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    // Test for movieViewModel
    private lateinit var viewModel: MovieViewModel
    private val repository: MovieRepository = mock()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        // Mock default behavior for init block
        whenever(repository.getMovies()).thenReturn(flowOf(emptyList()))
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test initial state
    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = MovieViewModel(repository)
        assertTrue(viewModel.uiState is MovieUiState.Loading)
    }

    // Test when movies are observed
    @Test
    fun `when movies are observed, uiState becomes Success`() = runTest {
        val movies = listOf(Movie(title = "Example1", posterpath = "", overview = ""))
        whenever(repository.getMovies()).thenReturn(flowOf(movies))
        
        viewModel = MovieViewModel(repository)
        advanceUntilIdle()

        assertTrue(viewModel.uiState is MovieUiState.Success)
        assertEquals(movies, (viewModel.uiState as MovieUiState.Success).movies)
    }

    // Test when movies are not observed
    @Test
    fun `searching with less than 2 characters returns all movies`() = runTest {
        val movies = listOf(
            Movie(title = "Inception", posterpath = "", overview = ""),
            Movie(title = "Batman", posterpath = "", overview = "")
        )
        whenever(repository.getMovies()).thenReturn(flowOf(movies))
        
        viewModel = MovieViewModel(repository)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("A")
        
        val currentState = viewModel.uiState as MovieUiState.Success
        assertEquals(2, currentState.movies.size)
    }


    // Test when movies are filtered
    @Test
    fun `searching with 2 or more characters filters movies`() = runTest {
        val movies = listOf(
            Movie(title = "Inception", posterpath = "", overview = ""),
            Movie(title = "Batman", posterpath = "", overview = "")
        )
        whenever(repository.getMovies()).thenReturn(flowOf(movies))
        
        viewModel = MovieViewModel(repository)
        advanceUntilIdle()

        viewModel.onSearchQueryChange("Bat")
        
        val currentState = viewModel.uiState as MovieUiState.Success
        assertEquals(1, currentState.movies.size)
        assertEquals("Batman", currentState.movies[0].title)
    }
}
