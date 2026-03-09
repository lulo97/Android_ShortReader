package com.example.shortreader.tabs

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shortreader.models.FavouriteItem

@Composable
fun FavouriteTab(activity: ComponentActivity) {

    val favourites = listOf(
        FavouriteItem(
            "Curiosity",
            "Strong desire to know something",
            "Her curiosity led her to explore the cave."
        ),
        FavouriteItem(
            "Brilliant",
            "Very intelligent",
            "She had a brilliant idea."
        )
    )

    var selectedItem by remember { mutableStateOf<FavouriteItem?>(null) }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        items(favourites) { item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { selectedItem = item }
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(item.word, style = MaterialTheme.typography.titleLarge)
                    Text(item.meaning)

                }

            }

        }

    }

    selectedItem?.let {

        AlertDialog(
            onDismissRequest = { selectedItem = null },
            confirmButton = {
                TextButton(onClick = { selectedItem = null }) {
                    Text("Close")
                }
            },
            title = { Text(it.word) },
            text = {
                Column {
                    Text("Meaning: ${it.meaning}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Example: ${it.example}")
                }
            }
        )

    }

}