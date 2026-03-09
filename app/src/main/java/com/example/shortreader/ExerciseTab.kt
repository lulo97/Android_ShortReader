package com.example.shortreader.tabs

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shortreader.ExerciseDetailActivity
import com.example.shortreader.models.Exercise

@Composable
fun ExerciseTab(activity: ComponentActivity) {

    val exercises = listOf(
        Exercise("Vocabulary Test"),
        Exercise("Reading Comprehension"),
        Exercise("Fill in the Blank")
    )

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