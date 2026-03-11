package com.example.android_engineer_technical_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.example.android_engineer_technical_assignment.ui.screens.DetailScreen
import com.example.android_engineer_technical_assignment.ui.screens.MovieScreen
import java.net.URLDecoder
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

@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    Android_Engineer_Technical_AssignmentTheme {
        MovieScreen(navController = rememberNavController())
    }
}