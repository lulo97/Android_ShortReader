package com.example.shortreader.database

import android.database.Cursor
import com.example.shortreader.models.WordDetail

data class WordDetailEntity(
    val id: Int,
    val word: String,
    val meaning: String,
    val example: String?,
    val partOfSpeech: String?,
    val pronunciation: String?
) {
    fun toWordDetail(): WordDetail {
        return WordDetail(
            word = word,
            meaning = meaning,
            example = example,
            partOfSpeech = partOfSpeech,
            pronunciation = pronunciation
        )
    }

    companion object {
        fun fromCursor(cursor: Cursor): WordDetailEntity {
            return WordDetailEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                word = cursor.getString(cursor.getColumnIndexOrThrow("word")),
                meaning = cursor.getString(cursor.getColumnIndexOrThrow("meaning")),
                example = cursor.getString(cursor.getColumnIndexOrThrow("example")),
                partOfSpeech = cursor.getString(cursor.getColumnIndexOrThrow("part_of_speech")),
                pronunciation = cursor.getString(cursor.getColumnIndexOrThrow("pronunciation"))
            )
        }
    }
}