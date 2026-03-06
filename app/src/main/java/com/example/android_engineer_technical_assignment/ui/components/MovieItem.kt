package com.example.android_engineer_technical_assignment.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_engineer_technical_assignment.data.Movie
import androidx.compose.ui.tooling.preview.Preview
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import androidx.compose.ui.graphics.Color

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier){
    Card(colors = CardDefaults.cardColors( containerColor = Color(0xFFD0BCFF)), modifier = modifier.padding(vertical = 25.dp).fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = movie.title,
            )
            Text(
                text = movie.description,
            )
        }
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

    Android_Engineer_Technical_AssignmentTheme {
        MovieItem(movie = movietest)
    }
}