package com.example.shortreader

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.shortreader.tabs.BookListTab
import com.example.shortreader.tabs.ExerciseTab
import com.example.shortreader.tabs.FavouriteTab
import com.example.shortreader.ui.theme.ShortReaderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    ) {

        when (selectedTab) {
            0 -> BookListTab(activity)
            1 -> FavouriteTab(activity)
            2 -> ExerciseTab(activity)
        }

    }
}