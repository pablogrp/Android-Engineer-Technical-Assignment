package com.example.android_engineer_technical_assignment

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.android_engineer_technical_assignment.data.AppDatabase
import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.data.MovieDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MovieDaoTest {
    // Local database
    private lateinit var db: AppDatabase
    private lateinit var dao: MovieDao

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Robolectric permite esto para simplificar tests
            .build()
        dao = db.movieDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    // Test to get movies in the database
    @Test
    fun getMovies()= runBlocking{
        val movies = dao.getAllMovies().first()
        Assert.assertTrue(movies.isEmpty())
    }

    @Test
    fun insertAndGetMovies() = runBlocking {
        val movie = Movie(title = "Example1", posterpath = "", overview = "")
        dao.insertAll(listOf(movie))

        val movies = dao.getAllMovies().first()

        Assert.assertEquals(1, movies.size)
        Assert.assertEquals("Example1", movies[0].title)
    }
}
