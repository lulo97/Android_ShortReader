package com.example.shortreader.tabs

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.shortreader.models.FavouriteItem
import com.example.shortreader.service.FavouriteService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun FavouriteTab(activity: ComponentActivity) {
    val favouriteService = FavouriteService(activity)
    var favourites by remember { mutableStateOf<List<FavouriteItem>>(emptyList()) }
    var selectedItem by remember { mutableStateOf<FavouriteItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        isLoading = true
        favouriteService.getAllFavourites().collect { favouriteList ->
            favourites = favouriteList
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(favourites) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { selectedItem = item }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(item.word, style = MaterialTheme.typography.titleLarge)
                            if (item.partOfSpeech != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "(${item.partOfSpeech})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Text(item.meaning)
                    }
                }
            }
        }
    }

    selectedItem?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            title = {
                Column {
                    Text(item.word)
                    if (item.pronunciation != null) {
                        Text(
                            text = item.pronunciation,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            text = {
                Column {
                    if (item.partOfSpeech != null) {
                        Text(
                            text = item.partOfSpeech,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text("Meaning: ${item.meaning}")

                    if (!item.example.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        val exampleText = item.example?.replace("\\n", "\n") ?: ""

                        Text("Example: ${exampleText}")
                    }

                    if (!item.notes.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("My notes: ${item.notes}")
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = {
                            activity.lifecycleScope.launch {
                                favouriteService.deleteFavourite(item.word).collect()
                                refreshTrigger++
                                selectedItem = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Remove")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { selectedItem = null }) {
                        Text("Close")
                    }
                }
            }
        )
    }
}