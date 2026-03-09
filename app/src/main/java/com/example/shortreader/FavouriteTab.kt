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
import kotlinx.coroutines.launch

@Composable
fun FavouriteTab(activity: ComponentActivity) {
    val favouriteService = FavouriteService(activity)
    var favourites by remember { mutableStateOf<List<FavouriteItem>>(emptyList()) }
    var selectedItem by remember { mutableStateOf<FavouriteItem?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        activity.lifecycleScope.launch {
            favouriteService.getAllFavourites().collect { favouriteList ->
                favourites = favouriteList
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