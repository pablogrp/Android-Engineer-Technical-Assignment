package com.example.android_engineer_technical_assignment.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_engineer_technical_assignment.data.Movie
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    val movietest = Movie(
        title = "Title 1",
        picture = "-----",
        description = "Description 1"
    )
    MovieItem(movie = movietest)
}