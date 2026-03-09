package com.example.shortreader.tabs

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.shortreader.BookDetailActivity
import com.example.shortreader.models.Book
import com.example.shortreader.service.BookService
import kotlinx.coroutines.launch

@Composable
fun BookListTab(activity: ComponentActivity) {
    val bookService = BookService(activity)
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        activity.lifecycleScope.launch {
            bookService.getAllBooks().collect { bookList ->
                books = bookList
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
}