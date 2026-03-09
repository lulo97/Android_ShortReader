package com.example.shortreader

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class BookDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("title") ?: ""
        val text = intent.getStringExtra("text") ?: ""

        setContent {
            MaterialTheme {
                BookDetailScreen(title, text)
            }
        }
    }
}

@Composable
fun BookDetailScreen(title: String, text: String) {

    var explainWord by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->

                val textView = TextView(context).apply {
                    this.text = text
                    textSize = 18f
                    setTextIsSelectable(true)
                }

                textView.setCustomSelectionActionModeCallback(
                    object : ActionMode.Callback {

                        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

                            menu?.add("Explain")
                                ?.setOnMenuItemClickListener {

                                    val start = textView.selectionStart
                                    val end = textView.selectionEnd

                                    if (start >= 0 && end > start) {
                                        val selected =
                                            textView.text.substring(start, end)

                                        explainWord = selected
                                    }

                                    mode?.finish()
                                    true
                                }

                            return true
                        }

                        override fun onPrepareActionMode(
                            mode: ActionMode?,
                            menu: Menu?
                        ): Boolean = false

                        override fun onActionItemClicked(
                            mode: ActionMode?,
                            item: MenuItem?
                        ): Boolean = false

                        override fun onDestroyActionMode(mode: ActionMode?) {}
                    }
                )

                textView
            }
        )
    }

    explainWord?.let { word ->
        WordExplainDialog(
            word = word,
            onDismiss = { explainWord = null }
        )
    }
}

@Composable
fun WordExplainDialog(
    word: String,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(word) },
        text = { Text(getFakeMeaning(word)) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

fun getFakeMeaning(word: String): String {

    return when (word.lowercase()) {

        "mysterious" ->
            "Difficult to understand or explain."

        "ancient" ->
            "Very old; from a long time ago."

        "legend" ->
            "A traditional story regarded as historical."

        else ->
            "Definition of \"$word\" not found."
    }
}