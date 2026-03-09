package com.example.android_engineer_technical_assignment.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.data.Movie
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0BCFF)),
        modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp).fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = movie.title ?: "Sin título",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.overview ?: "Sin descripción disponible.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (movie.poster_path != null) {
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Póster de ${movie.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Altura de la imagen
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    val movietest = Movie(
        title = "Movie test",
        poster_path = null,
        overview = "overview."
    )
    Android_Engineer_Technical_AssignmentTheme {
        MovieItem(movie = movietest)
    }
}