package com.example.android_engineer_technical_assignment.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // IMPORTANTE PARA EL ESTADO
import androidx.compose.runtime.setValue // IMPORTANTE PARA EL ESTADO
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite // EL CORAZÓN RELLENO
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DetailScreen(
    title: String,
    posterPath: String,
    overview: String,
    onBack: () -> Unit
) {
    // 🧠 El estado visual que controla si está pulsado o no
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f) // this column will take all the space
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically, // Los alinea a la misma altura
                    horizontalArrangement = Arrangement.Center // Centra la fila entera
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxWidth(0.8f), // Ocupa el 80%
                        textAlign = TextAlign.Center // Centra el texto dentro de su 80%
                    )

                    IconButton(
                        onClick = {
                            // 1. Cambiamos el estado (si era false, pasa a true, y viceversa)
                            isFavorite = !isFavorite

                            // 2. Dejamos el chivato listo para tu Base de Datos Local
                            if (isFavorite) {
                                println("Guardar '$title' en la base de datos local (Room)")
                            } else {
                                println("Borrar '$title' de la base de datos local")
                            }
                        }
                    ) {
                        // El Icono reacciona al estado "isFavorite" en tiempo real
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Añadir a favoritos",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (posterPath != "null") {
                    val imageUrl = if (posterPath.startsWith("http")) posterPath
                    else "https://image.tmdb.org/t/p/w500/$posterPath"

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Poster",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp) // Imagen grande
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
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
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBack
            ) {
                Text(text = "Return")
            }
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
            onBack = {}
        )
    }
}