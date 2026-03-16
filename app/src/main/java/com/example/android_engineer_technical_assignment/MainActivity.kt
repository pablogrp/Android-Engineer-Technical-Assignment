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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.android_engineer_technical_assignment.data.AppDatabase
import com.example.android_engineer_technical_assignment.data.DB.FavoriteMovie
import com.example.android_engineer_technical_assignment.ui.screens.DetailScreen
import com.example.android_engineer_technical_assignment.ui.screens.FavoritesScreen
import com.example.android_engineer_technical_assignment.ui.screens.MovieScreen
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import com.example.android_engineer_technical_assignment.viewmodel.FavoriteViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val movie_bd = Room.databaseBuilder(
        //    applicationContext,
        //    AppDatabase::class.java, "movies-db")
        //    .fallbackToDestructiveMigration(false).build()

        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "favorites-db"
            ).fallbackToDestructiveMigration(false)
            .build()

        val favoriteViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavoriteViewModel(db.movieDao()) as T
            }
        }

        enableEdgeToEdge()
        setContent {
            Android_Engineer_Technical_AssignmentTheme {
                val navController = rememberNavController()

                val favoriteViewModel: FavoriteViewModel = viewModel(factory = favoriteViewModelFactory)
                val favorites by favoriteViewModel.favoriteMovies.collectAsState()

                NavHost(navController = navController, startDestination = "movie_list") {

                    composable("movie_list") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            MovieScreen(navController = navController, modifier = Modifier.padding(innerPadding))
                        }
                    }

                    composable("more_info/{title}/{poster}/{overview}") { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title") ?: "No title"
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
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("favorites") {
                        FavoritesScreen(
                            viewModel = favoriteViewModel,
                            onMovieClick = { movie ->
                                val encodedOverview = URLEncoder.encode(movie.overview, StandardCharsets.UTF_8.toString())
                                navController.navigate("more_info/${movie.title}/${movie.posterPath}/$encodedOverview")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}