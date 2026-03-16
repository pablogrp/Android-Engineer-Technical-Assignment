package com.example.android_engineer_technical_assignment.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.data.DB.Movie
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme

/**
 * UI function to display a movie card
 *
 * @param movie object with the main information of the movie
 * @param onSeeMoreClick Lambda function to select the movie
 * @param modifier optional modifiers to be applied
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(movie: Movie, onSeeMoreClick: () -> Unit, modifier: Modifier = Modifier) {

    // Experimental card which shows a shadow to give more volume
    ElevatedCard(
        onClick = onSeeMoreClick,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        // Row direction to split in 2 different sides
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (movie.posterpath != null) {
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterpath}"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "${movie.title} poster",
                    modifier = Modifier
                        .width(110.dp)
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            // If there's no picture we create a gray rectangle instead
            } else {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(160.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ){}
            }


            // With the column we can split de card in 2
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = movie.title ?: "No title available",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movie.overview ?: "No overview available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {

    // Example movie to see in the preview
    val movietest = Movie(
        title = "Movie",
        posterpath = null,
        overview = "Movie Overview"
    )
    Android_Engineer_Technical_AssignmentTheme {
        MovieItem(
            movie = movietest,
            onSeeMoreClick = { }
        )
    }
}