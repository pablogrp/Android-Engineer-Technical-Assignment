package com.example.android_engineer_technical_assignment.api

import com.example.android_engineer_technical_assignment.data.MovieService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import org.junit.Assert.assertEquals
import retrofit2.converter.gson.GsonConverterFactory

class MovieServiceTest{
    private lateinit var mockWebServer: MockWebServer
    private lateinit var movieService: MovieService

    @Before
    fun setup(){
        // Initialize fake server
        mockWebServer = MockWebServer()

        // Create retrofit
        movieService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun `test getMovies` () = runBlocking {
        val mockJsonResponse = """
            {
                "page": 1,
                "results": [
                    {
                        "title": "Example1",
                        "overview": "",
                        "poster_path": ""
                    }
                ]
            }
        """.trimIndent()

        //Load fake server
        mockWebServer.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200)) // 200 its de code for everythin is fine
        
        val response = movieService.getMovies(page = 1)
        
        assertEquals(1, response.results.size)
        assertEquals("Example1", response.results[0].title)
    }

    @Test
    fun `test searchMovies` () = runBlocking {
        val mockJsonResponse = """
            {
                "page": 1,
                "results": [
                    {
                        "title": "Example2",
                        "overview": "",
                        "poster_path": ""
                    }
                ]
            }
        """.trimIndent()


        mockWebServer.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(200))

        val response = movieService.searchMovies(query = "Example2")
        
        assertEquals(1, response.results.size)
        assertEquals("Example2", response.results[0].title)
    }
}