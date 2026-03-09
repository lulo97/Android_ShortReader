package com.example.shortreader.repository

import android.content.ContentValues
import android.content.Context
import com.example.shortreader.database.DatabaseHelper
import com.example.shortreader.database.WordDetailEntity
import com.example.shortreader.models.WordDetail

class WordDetailRepository(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun findWordDetail(word: String): WordDetail? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM word_details WHERE word = ? COLLATE NOCASE",
            arrayOf(word.trim())
        )

        try {
            if (cursor.moveToFirst()) {
                val entity = WordDetailEntity.fromCursor(cursor)
                return entity.toWordDetail()
            }
        } finally {
            cursor.close()
            db.close()
        }
        return null
    }

    fun searchWords(query: String): List<WordDetail> {
        val results = mutableListOf<WordDetail>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM word_details WHERE word LIKE ? OR meaning LIKE ? ORDER BY word",
            arrayOf("%$query%", "%$query%")
        )

        try {
            while (cursor.moveToNext()) {
                val entity = WordDetailEntity.fromCursor(cursor)
                results.add(entity.toWordDetail())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return results
    }

    fun addWordDetail(wordDetail: WordDetail): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("word", wordDetail.word)
            put("meaning", wordDetail.meaning)
            put("example", wordDetail.example)
            put("part_of_speech", wordDetail.partOfSpeech)
            put("pronunciation", wordDetail.pronunciation)
        }

        val result = db.insertWithOnConflict("word_details", null, values, android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE)
        db.close()
        return result
    }
}