package com.example.shortreader.database

import android.database.Cursor
import com.example.shortreader.models.Exercise

data class ExerciseEntity(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String
) {
    fun toExercise(): Exercise {
        return Exercise(title)
    }

    companion object {
        fun fromCursor(cursor: Cursor): ExerciseEntity {
            return ExerciseEntity(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"))
            )
        }
    }
}