package com.example.android_engineer_technical_assignment.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DetailScreen(
    title: String,
    posterPath: String,
    overview: String,
    onBack: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (posterPath != "null") {
                val imageUrl = "https://image.tmdb.org/t/p/w500/$posterPath"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = overview,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text(text = "Return")
            }
        }
    }
}
