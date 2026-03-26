package com.example.android_engineer_technical_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.ui.screens.DetailScreen
import com.example.android_engineer_technical_assignment.ui.screens.FavoritesScreen
import com.example.android_engineer_technical_assignment.ui.screens.MovieScreen
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import com.example.android_engineer_technical_assignment.viewmodel.FavoriteViewModel
import com.example.android_engineer_technical_assignment.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Android_Engineer_Technical_AssignmentTheme {
                val navController = rememberNavController()


                val favoriteViewModel: FavoriteViewModel = hiltViewModel()
                val movieViewModel: MovieViewModel = hiltViewModel()

                val favorites by favoriteViewModel.favoriteMovies.collectAsState()

                NavHost(navController = navController, startDestination = "movie_list") {

                    composable("movie_list") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MovieScreen(
                                navController = navController,
                                modifier = Modifier.padding(innerPadding),
                                movieViewModel = movieViewModel,
                                favoriteViewModel = favoriteViewModel
                            )
                        }
                    }

                    composable("more_info/{title}/{poster}/{overview}") { backStackEntry ->
                        val rawTitle = backStackEntry.arguments?.getString("title") ?: "No title"
                        val title = URLDecoder.decode(rawTitle, StandardCharsets.UTF_8.toString())
                        val poster = backStackEntry.arguments?.getString("poster") ?: ""
                        val rawOverview = backStackEntry.arguments?.getString("overview") ?: ""
                        val cleanOverview = URLDecoder.decode(rawOverview, StandardCharsets.UTF_8.toString())

                        val isFav = favorites.any { it.title == title }

                        DetailScreen(
                            title = title,
                            posterPath = poster,
                            overview = cleanOverview,
                            isFavorite = isFav,
                            onToggleFavorite = {
                                val movie = FavoriteMovie(title, poster, cleanOverview)
                                favoriteViewModel.toggleFavorite(movie)
                            },
                            onBack = { navController.popBackStack() },
                            onFavoriteClick = {
                                val movie = FavoriteMovie(title, poster, cleanOverview)
                                favoriteViewModel.toggleFavorite(movie)
                            }
                        )
                    }

                    composable("favorites") {
                        FavoritesScreen(
                            viewModel = favoriteViewModel,
                            onMovieClick = { movie ->
                                val encodedTitle = URLEncoder.encode(movie.title, StandardCharsets.UTF_8.toString())
                                val encodedOverview = URLEncoder.encode(movie.overview, StandardCharsets.UTF_8.toString())
                                navController.navigate("more_info/$encodedTitle/${movie.posterPath}/$encodedOverview")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
