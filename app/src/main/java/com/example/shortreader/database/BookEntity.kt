package com.example.shortreader.database

import android.database.Cursor
import com.example.shortreader.models.Book

data class BookEntity(
    val id: Int,
    val title: String,
    val wordCount: Int,
    val preview: String,
    val fullText: String
) {
    fun toBook(): Book {
        return Book(title, wordCount, preview, fullText)
    }

    companion object {
        fun fromCursor(cursor: Cursor): BookEntity {
            return BookEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                wordCount = cursor.getInt(cursor.getColumnIndexOrThrow("word_count")),
                preview = cursor.getString(cursor.getColumnIndexOrThrow("preview")),
                fullText = cursor.getString(cursor.getColumnIndexOrThrow("full_text"))
            )
        }
    }
}