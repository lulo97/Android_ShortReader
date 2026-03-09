package com.example.shortreader.tabs

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shortreader.BookDetailActivity
import com.example.shortreader.models.Book

@Composable
fun BookListTab(activity: ComponentActivity) {

    val books = listOf(
        Book(
            "Alice in Wonderland",
            500,
            "Alice was beginning to get very tired of sitting...",
            "Full book content here..."
        ),
        Book(
            "Sherlock Holmes",
            800,
            "To Sherlock Holmes she is always the woman...",
            "Full Sherlock Holmes text..."
        )
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        items(books) { book ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable {

                        val intent = Intent(activity, BookDetailActivity::class.java)
                        intent.putExtra("title", book.title)
                        intent.putExtra("text", book.fullText)
                        activity.startActivity(intent)

                    }
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(book.title, style = MaterialTheme.typography.titleLarge)

                    Text("Words: ${book.wordCount}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(book.preview)
                }
            }

        }

    }

}