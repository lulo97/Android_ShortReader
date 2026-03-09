package com.example.shortreader.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.shortreader.database.BookEntity
import com.example.shortreader.database.DatabaseHelper
import com.example.shortreader.models.Book

class BookRepository(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM books ORDER BY title", null)

        try {
            while (cursor.moveToNext()) {
                val entity = BookEntity.fromCursor(cursor)
                books.add(entity.toBook())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return books
    }

    fun getBookById(id: Int): Book? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM books WHERE id = ?", arrayOf(id.toString()))

        try {
            if (cursor.moveToFirst()) {
                val entity = BookEntity.fromCursor(cursor)
                return entity.toBook()
            }
        } finally {
            cursor.close()
            db.close()
        }

        return null
    }

    fun searchBooks(query: String): List<Book> {
        val books = mutableListOf<Book>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM books WHERE title LIKE ? OR preview LIKE ? ORDER BY title",
            arrayOf("%$query%", "%$query%")
        )

        try {
            while (cursor.moveToNext()) {
                val entity = BookEntity.fromCursor(cursor)
                books.add(entity.toBook())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return books
    }
}