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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_engineer_technical_assignment.ui.screens.DetailScreen
import com.example.android_engineer_technical_assignment.ui.screens.FavoritesScreen
import com.example.android_engineer_technical_assignment.ui.screens.MovieScreen
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import com.example.android_engineer_technical_assignment.viewmodel.DetailViewModel
import com.example.android_engineer_technical_assignment.viewmodel.FavoriteViewModel
import com.example.android_engineer_technical_assignment.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
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

                    composable(
                        route = "more_info/{movieTitle}",
                        arguments = listOf(navArgument("movieTitle") { type = NavType.StringType })
                    ) {
                        val detailViewModel: DetailViewModel = hiltViewModel()
                        
                        DetailScreen(
                            viewModel = detailViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("favorites") {
                        FavoritesScreen(
                            viewModel = favoriteViewModel,
                            onMovieClick = { movie ->
                                navController.navigate("more_info/${movie.title}")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
