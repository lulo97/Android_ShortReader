package com.example.shortreader.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.shortreader.database.DatabaseHelper
import com.example.shortreader.database.FavouriteWordEntity
import com.example.shortreader.models.FavouriteItem

class FavouriteRepository(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun getAllFavourites(): List<FavouriteItem> {
        val favourites = mutableListOf<FavouriteItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM favourite_words ORDER BY word", null)

        try {
            while (cursor.moveToNext()) {
                val entity = FavouriteWordEntity.fromCursor(cursor)
                favourites.add(entity.toFavouriteItem())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return favourites
    }

    fun addFavourite(item: FavouriteItem): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("word", item.word)
            put("meaning", item.meaning)
            put("example", item.example)
        }

        val result = db.insertWithOnConflict("favourite_words", null, values, SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
        return result != -1L
    }

    fun deleteFavourite(word: String): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete("favourite_words", "word = ?", arrayOf(word))
        db.close()
        return result
    }

    fun searchFavourites(query: String): List<FavouriteItem> {
        val favourites = mutableListOf<FavouriteItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM favourite_words WHERE word LIKE ? OR meaning LIKE ? ORDER BY word",
            arrayOf("%$query%", "%$query%")
        )

        try {
            while (cursor.moveToNext()) {
                val entity = FavouriteWordEntity.fromCursor(cursor)
                favourites.add(entity.toFavouriteItem())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return favourites
    }
}