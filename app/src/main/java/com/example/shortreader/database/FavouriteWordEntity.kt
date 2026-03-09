package com.example.shortreader.database

import android.database.Cursor
import com.example.shortreader.models.FavouriteItem

data class FavouriteWordEntity(
    val id: Int,
    val word: String,
    val meaning: String,
    val example: String
) {
    fun toFavouriteItem(): FavouriteItem {
        return FavouriteItem(word, meaning, example)
    }

    companion object {
        fun fromCursor(cursor: Cursor): FavouriteWordEntity {
            return FavouriteWordEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                word = cursor.getString(cursor.getColumnIndexOrThrow("word")),
                meaning = cursor.getString(cursor.getColumnIndexOrThrow("meaning")),
                example = cursor.getString(cursor.getColumnIndexOrThrow("example"))
            )
        }
    }
}