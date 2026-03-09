package com.example.android_engineer_technical_assignment.ui

import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

val retrofit = Retrofit.Builder()
    .baseUrl(Constant.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// Train list to test (not in use)
//val trainlist = listOf(
//    Movie("Tittle 1", "examplepictureurl1", "description 1"),
//    Movie("Tittle 2", "examplepictureurl2", "description 2")
//)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android_Engineer_Technical_AssignmentTheme {
                val navController = rememberNavController()
                androidx.navigation.compose.NavHost(
                    navController = navController,
                    startDestination = "movie_list"
                ) {
                    composable("movie_list"){
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MovieScreen(navController= navController, modifier = Modifier.padding(innerPadding))
                        }
                    }
                    composable("more_info"){
                        DetailScreen(onBack = {navController.popBackStack()})
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
        MovieScreen(navController = rememberNavController())
    }
}

// Main screen whith all the movies
@Composable
fun MovieScreen(navController: androidx.navigation.NavHostController, modifier: Modifier = Modifier) {
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
                        MovieItem(
                            movie = currentMovie,
                            onSeeMoreClick = {
                                navController.navigate("more_info")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(onBack: () -> Unit){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "new screen test")
            androidx.compose.material3.Button(onClick = onBack) {
                Text(text = "return")
            }
        }
    }
}


