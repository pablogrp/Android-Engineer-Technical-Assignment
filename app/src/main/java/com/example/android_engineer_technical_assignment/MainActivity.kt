package com.example.android_engineer_technical_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_engineer_technical_assignment.ui.theme.Android_Engineer_Technical_AssignmentTheme
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth

// Train list to test
val trainlist = listOf<Movie>(
    Movie("1", "examplepictureurl1", "description 1"),
    Movie("2", "examplepictureurl2", "description 2"),
    Movie("3", "examplepictureurl3", "description 3"),
    Movie("4", "examplepictureurl4", "description 4"),
    Movie("5", "examplepictureurl5", "description 5")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android_Engineer_Technical_AssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/*
   @Composable
   fun MovieList(modifier: Modifier = Modifier){
       for (i in train_list.indices){
           Log.d("MovieList", train_list[i].toString())
       }
   }
   */
@Preview(showBackground = true)
@Composable
fun MovieListPreview() {
    Android_Engineer_Technical_AssignmentTheme {
        MovieList()
    }
}
@Composable
fun MovieList(modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier.padding(24.dp)) {
        items(trainlist){ movie ->
            Column(
                modifier = Modifier
                    .padding(vertical = 50.dp)
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
                Text(
                    text = movie.picture
                )
            }
        }
    }
}