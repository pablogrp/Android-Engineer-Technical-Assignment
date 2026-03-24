package com.example.android_engineer_technical_assignment.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
 * UI function to display a movie card with Favorite toggle
 *
 * @param movie object with the main information of the movie
 * @param isFavorite whether the movie is marked as favorite
 * @param onSeeMoreClick Lambda function to select the movie
 * @param onFavoriteClick Lambda function to toggle favorite
 * @param modifier optional modifiers to be applied
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    movie: Movie,
    isFavorite: Boolean,
    onSeeMoreClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

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
        FavoriteConfirmationDialog(
            show = showDialog,
            onConfirm = {
                onFavoriteClick()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )

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
            } else {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(160.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ){}
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = movie.title ?: "No title available",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = { showDialog = true }) {
                        val scale by animateFloatAsState(
                            targetValue = if (isFavorite) 1.4f else 1.0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )

                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                        )
                    }
                }

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

/**
 * UI function to display a confirmation dialog
 * @param show whether the dialog is shown
 * @param onConfirm Lambda function to confirm the action
 * @param onDismiss Lambda function to dismiss
 */
@Composable
fun FavoriteConfirmationDialog(
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Decline", color = MaterialTheme.colorScheme.error)
                }
            },
            title = {
                Text(text = "Confirmation")
            },
            text = {
                Text(text = "Are you sure you want to add/remove this movie to/from favorites?")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    val movietest = Movie(
        title = "Movie",
        posterpath = null,
        overview = "Movie Overview"
    )
    Android_Engineer_Technical_AssignmentTheme {
        MovieItem(
            movie = movietest,
            isFavorite = true,
            onSeeMoreClick = { },
            onFavoriteClick = { }
        )
    }
}
