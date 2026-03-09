package com.example.shortreader.database

import android.database.Cursor
import com.example.shortreader.models.FavouriteItem

data class FavouriteWordEntity(
    val id: Int,
    val wordDetailId: Int,
    val notes: String?,
    val wordDetail: WordDetailEntity? = null
) {
    fun toFavouriteItem(): FavouriteItem {
        return FavouriteItem(
            word = wordDetail?.word ?: "",
            meaning = wordDetail?.meaning ?: "",
            example = wordDetail?.example ?: "",
            notes = notes,
            partOfSpeech = wordDetail?.partOfSpeech,
            pronunciation = wordDetail?.pronunciation
        )
    }

    companion object {
        fun fromCursor(cursor: Cursor): FavouriteWordEntity {
            // Try to get word details if joined
            val wordDetail = try {
                if (cursor.getColumnIndex("word") >= 0) {
                    WordDetailEntity(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("word_detail_id")),
                        word = cursor.getString(cursor.getColumnIndexOrThrow("word")),
                        meaning = cursor.getString(cursor.getColumnIndexOrThrow("meaning")),
                        example = cursor.getString(cursor.getColumnIndexOrThrow("example")),
                        partOfSpeech = cursor.getString(cursor.getColumnIndexOrThrow("part_of_speech")),
                        pronunciation = cursor.getString(cursor.getColumnIndexOrThrow("pronunciation"))
                    )
                } else null
            } catch (e: Exception) {
                null
            }

            return FavouriteWordEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                wordDetailId = cursor.getInt(cursor.getColumnIndexOrThrow("word_detail_id")),
                notes = cursor.getString(cursor.getColumnIndexOrThrow("notes")),
                wordDetail = wordDetail
            )
        }
    }
}