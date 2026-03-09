package com.example.android_engineer_technical_assignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.android_engineer_technical_assignment.data.Movie
import com.example.android_engineer_technical_assignment.ui.components.MovieItem
import com.example.android_engineer_technical_assignment.data.Constant
import com.example.android_engineer_technical_assignment.data.MovieService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.android_engineer_technical_assignment.data.RetrofitClient
import androidx.compose.material3.CircularProgressIndicator

val retrofit = Retrofit.Builder()
    .baseUrl(Constant.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(MovieService::class.java)

// Train list to test
val trainlist = listOf(
    Movie("Tittle 1", "examplepictureurl1", "description 1"),
    Movie("Tittle 2", "examplepictureurl2", "description 2")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android_Engineer_Technical_AssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MovieScreen(modifier: Modifier = Modifier) {
    var movieList by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getMovies()
            movieList = response.results
            isLoading = false
        } catch (e: Exception) {
            e.printStackTrace()
            isError = true
            isLoading = false
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            isError -> Text(text = "Error.")
            movieList.isEmpty() -> Text(text = "No movie data available.")
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(movieList) { currentMovie ->
                        MovieItem(movie = currentMovie)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    Android_Engineer_Technical_AssignmentTheme {
        MovieScreen()
    }
}

