package com.example.shortreader

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.example.shortreader.models.WordDetail
import com.example.shortreader.repository.WordDetailRepository
import kotlinx.coroutines.launch

class BookDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("title") ?: ""
        val text = intent.getStringExtra("text") ?: ""

        setContent {
            MaterialTheme {
                BookDetailScreen(title, text, this)
            }
        }
    }
}

@Composable
fun BookDetailScreen(title: String, text: String, activity: ComponentActivity) {

    var selectedWord by remember { mutableStateOf<String?>(null) }
    var wordDetail by remember { mutableStateOf<WordDetail?>(null) }
    val wordDetailRepository = WordDetailRepository(activity)
    if (!text.contains("\n")) {
        throw IllegalArgumentException(
            "Invalid text format: Missing newline characters.\n" +
                    "Expected: Multiline text with '\\n' separators\n" +
                    "Received: '$text' (length: ${text.length})"
        )
    }
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

                    // Print the text to Logcat
                    Log.d("TextViewDebug", "Text content: '$text'")
                    // Or to see escaped characters:
                    Log.d("TextViewDebug", "Text with escapes: " + text.replace("\n", "\\n"))


                    textSize = 18f
                    setTextIsSelectable(true)
                    setLineSpacing(8f, 1.2f)
                }

                textView.setCustomSelectionActionModeCallback(
                    object : ActionMode.Callback {

                        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

                            menu?.add("Explain")
                                ?.setOnMenuItemClickListener {

                                    val start = textView.selectionStart
                                    val end = textView.selectionEnd

                                    if (start >= 0 && end > start) {
                                        val selected = textView.text
                                            .substring(start, end)
                                            .trim()
                                            .lowercase()

                                        selectedWord = selected

                                        // Look up the word meaning from database
                                        activity.lifecycleScope.launch {
                                            wordDetail = wordDetailRepository.findWordDetail(selected)
                                        }
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

    if (selectedWord != null) {
        WordExplainDialog(
            word = selectedWord!!,
            wordDetail = wordDetail,
            onDismiss = {
                selectedWord = null
                wordDetail = null
            }
        )
    }
}

@Composable
fun WordExplainDialog(
    word: String,
    wordDetail: WordDetail?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(text = word)
                if (wordDetail?.pronunciation != null) {
                    Text(
                        text = wordDetail.pronunciation,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        text = {
            if (wordDetail != null) {
                Column {
                    if (wordDetail.partOfSpeech != null) {
                        Text(
                            text = wordDetail.partOfSpeech,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = wordDetail.meaning,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (!wordDetail.example.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Example:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        val exampleText = wordDetail.example?.replace("\\n", "\n") ?: ""

                        Text(
                            text = exampleText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Text(
                    text = "Definition of \"$word\" not found in the dictionary.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}