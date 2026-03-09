package com.example.shortreader.repository

import android.content.Context
import com.example.shortreader.database.DatabaseHelper
import com.example.shortreader.database.ExerciseEntity
import com.example.shortreader.models.Exercise

class ExerciseRepository(private val context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun getAllExercises(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM exercises ORDER BY title", null)

        try {
            while (cursor.moveToNext()) {
                val entity = ExerciseEntity.fromCursor(cursor)
                exercises.add(entity.toExercise())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return exercises
    }

    fun getExercisesByDifficulty(difficulty: String): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM exercises WHERE difficulty = ? ORDER BY title",
            arrayOf(difficulty)
        )

        try {
            while (cursor.moveToNext()) {
                val entity = ExerciseEntity.fromCursor(cursor)
                exercises.add(entity.toExercise())
            }
        } finally {
            cursor.close()
            db.close()
        }

        return exercises
    }
}