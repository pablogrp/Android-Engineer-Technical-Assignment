package com.example.android_engineer_technical_assignment.ui

import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Button
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import coil.compose.AsyncImage
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

//val retrofit = Retrofit.Builder()
//    .baseUrl(Constant.BASE_URL)
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()

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

                // Nav map
                NavHost(navController = navController, startDestination = "movie_list") {

                    // Main screen
                    composable("movie_list") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MovieScreen(navController = navController, modifier = Modifier.padding(innerPadding))
                        }
                    }

                    // Second screen
                    composable("more_info/{title}/{poster}/{overview}") { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title") ?: "No title"
                        val poster = backStackEntry.arguments?.getString("poster") ?: ""


                        val rawOverview = backStackEntry.arguments?.getString("overview") ?: ""
                        val cleanOverview = URLDecoder.decode(rawOverview, StandardCharsets.UTF_8.toString())

                        DetailScreen(
                            title = title,
                            posterPath = poster,
                            overview = cleanOverview,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var movieList by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getMovies()
            movieList = response.results
            isLoading = false
        } catch (e: Exception) {
            isError = true
            isLoading = false
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            isError -> Text(text = "Error al cargar datos.")
            movieList.isEmpty() -> Text(text = "No hay películas disponibles.")
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(movieList) { currentMovie ->
                        MovieItem(
                            movie = currentMovie,
                            onSeeMoreClick = {
                                // Limpiamos el póster (quitamos la / inicial)
                                val cleanPoster = currentMovie.posterpath?.removePrefix("/") ?: "null"

                                // Codificamos la descripción para que no rompa la URL con espacios o símbolos
                                val encodedOverview = URLEncoder.encode(
                                    currentMovie.overview ?: "No description",
                                    StandardCharsets.UTF_8.toString()
                                )

                                navController.navigate("more_info/${currentMovie.title}/$cleanPoster/$encodedOverview")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(
    title: String,
    posterPath: String,
    overview: String,
    onBack: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (posterPath != "null") {
                val imageUrl = "https://image.tmdb.org/t/p/w500/$posterPath"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = overview,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text(text = "Return")
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