package com.example.shortreader.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.shortreader.database.DatabaseHelper
import com.example.shortreader.database.FavouriteWordEntity
import com.example.shortreader.models.FavouriteItem
import com.example.shortreader.models.WordDetail

class FavouriteRepository(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val wordDetailRepository = WordDetailRepository(context)

    fun getAllFavourites(): List<FavouriteItem> {
        val favourites = mutableListOf<FavouriteItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            """SELECT f.*, w.word, w.meaning, w.example, w.part_of_speech, w.pronunciation 
               FROM favourite_words f
               JOIN word_details w ON f.word_detail_id = w.id
               ORDER BY w.word""",
            null
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

    fun addFavourite(item: FavouriteItem): Boolean {
        // First, check if word detail exists
        var wordDetail = wordDetailRepository.findWordDetail(item.word)

        if (wordDetail == null) {
            // Create new word detail
            wordDetail = WordDetail(
                word = item.word,
                meaning = item.meaning,
                example = item.example
            )
            wordDetailRepository.addWordDetail(wordDetail)
            wordDetail = wordDetailRepository.findWordDetail(item.word)
        }

        wordDetail?.let { detail ->
            // Get the word_detail_id
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT id FROM word_details WHERE word = ?",
                arrayOf(item.word)
            )

            var wordDetailId: Int? = null
            if (cursor.moveToFirst()) {
                wordDetailId = cursor.getInt(0)
            }
            cursor.close()
            db.close()

            wordDetailId?.let { id ->
                // Add to favourites
                val writableDb = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("word_detail_id", id)
                    put("notes", item.notes)
                }

                val result = writableDb.insertWithOnConflict(
                    "favourite_words",
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE
                )
                writableDb.close()
                return result != -1L
            }
        }

        return false
    }

    fun deleteFavourite(word: String): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            "favourite_words",
            "word_detail_id IN (SELECT id FROM word_details WHERE word = ?)",
            arrayOf(word)
        )
        db.close()
        return result
    }

    fun searchFavourites(query: String): List<FavouriteItem> {
        val favourites = mutableListOf<FavouriteItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            """SELECT f.*, w.word, w.meaning, w.example, w.part_of_speech, w.pronunciation 
               FROM favourite_words f
               JOIN word_details w ON f.word_detail_id = w.id
               WHERE w.word LIKE ? OR w.meaning LIKE ?
               ORDER BY w.word""",
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