package com.example.shortreader.tabs

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.shortreader.ExerciseDetailActivity
import com.example.shortreader.models.Exercise
import com.example.shortreader.service.ExerciseService
import kotlinx.coroutines.launch

@Composable
fun ExerciseTab(activity: ComponentActivity) {
    val exerciseService = ExerciseService(activity)
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        activity.lifecycleScope.launch {
            exerciseService.getAllExercises().collect { exerciseList ->
                exercises = exerciseList
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(exercises) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable {
                            val intent = Intent(activity, ExerciseDetailActivity::class.java)
                            intent.putExtra("title", exercise.title)
                            activity.startActivity(intent)
                        }
                ) {
                    Text(
                        exercise.title,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}