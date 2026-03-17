package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import android.R.attr.navigationIcon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme

/**
 * UI screen that shows the details of the movies and allow the "favourite movie" management
 *
 * @param String title, name of the movie
 * @param String posterPath, posterPath URL
 * @param String overview, small description of the movie
 * @param Boolean isFavourite, indicates if the movie is marked as favourite or not
 * @param Unit onToggleFavourite, Callback when the favourite icon is touched
 * @param Unit onBack, Callback to return to the main page
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(title: String, posterPath: String, overview: String, isFavorite: Boolean, onToggleFavorite: () -> Unit,onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                            contentDescription = "Return"
                        )
                    }
                },

                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall, // Letra un poco más pequeña para que quepa bien
                        textAlign = TextAlign.Center
                    )
                },

                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Add to Favourites",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { padding ->
        // --- Title and icon section ---
        Column(
            modifier = Modifier
                .fillMaxSize() // takes al the available space pushing the "Return" bottom to the end
                .padding(padding) // Añadido para que no se solape con la TopBar
                .padding(horizontal = 16.dp) // Un poco de aire a los lados
                .verticalScroll(rememberScrollState()), // Scrollable in case of long overviews
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // --- Poster section ---
            if (posterPath != "null") {
                val imageUrl = if (posterPath.startsWith("http")) posterPath
                else "https://image.tmdb.org/t/p/w500/$posterPath"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            // --- Overview Section ---
            Text(
                text = overview,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Android_Engineer_Technical_AssignmentTheme {
        DetailScreen(
            title = "Example movie title",
            posterPath = "",
            overview = "Example movie overview",
            isFavorite = false,
            onToggleFavorite = {},
            onBack = {}
        )
    }
}