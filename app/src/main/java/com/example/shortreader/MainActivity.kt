package com.example.shortreader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.shortreader.database.DatabaseHelper
import com.example.shortreader.tabs.BookListTab
import com.example.shortreader.tabs.ExerciseTab
import com.example.shortreader.tabs.FavouriteTab
import com.example.shortreader.ui.theme.ShortReaderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database with better error handling
        try {
            val dbHelper = DatabaseHelper(this)
            val database = dbHelper.writableDatabase

            // Check if database was properly populated
            if (!dbHelper.isDatabasePopulated()) {
                Log.e("MainActivity", "Database not properly populated!")
                // You might want to show an error message to the user
            } else {
                Log.d("MainActivity", "Database initialized successfully")
            }

            database.close()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing database", e)
        }

        setContent {
            ShortReaderTheme {
                MainScreen(this)
            }
        }
    }
}

@Composable
fun MainScreen(activity: ComponentActivity) {

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Books", "Favourite", "Exercise")

    Scaffold(
        bottomBar = {
            NavigationBar {

                tabs.forEachIndexed { index, title ->

                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = { Text(title) },
                        icon = { }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Use the padding values provided by Scaffold
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> BookListTab(activity)
                1 -> FavouriteTab(activity)
                2 -> ExerciseTab(activity)
            }
        }
    }
}