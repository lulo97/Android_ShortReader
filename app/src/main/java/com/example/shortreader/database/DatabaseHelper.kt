package com.example.shortreader.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.shortreader.utils.DevConfig
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shortreader.db"
        private const val DATABASE_VERSION = 1
        private const val TAG = "DatabaseHelper"
    }

    private val appContext = context.applicationContext

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating database and executing seed scripts")
        executeSqlScript(db, "database/01_create_tables.sql")
        executeSqlScript(db, "database/02_seed_books.sql")
        executeSqlScript(db, "database/03_seed_exercises.sql")
        executeSqlScript(db, "database/04_seed_favourites.sql")
        Log.d(TAG, "Database creation completed")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from $oldVersion to $newVersion")
        recreateDatabase(db)
        onCreate(db)  // Recreate tables after dropping
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)

        // In development mode, check if we should recreate the database
        if (DevConfig.DROP_DATABASE_ON_START) {
            Log.d(TAG, "Development mode: Recreating database from scratch")

            // Drop all tables
            recreateDatabase(db)

            // Recreate tables and seed data
            onCreate(db)
        }
    }

    private fun recreateDatabase(db: SQLiteDatabase) {
        Log.d(TAG, "Recreating database - dropping all tables")
        db.execSQL("DROP TABLE IF EXISTS books")
        db.execSQL("DROP TABLE IF EXISTS exercises")
        db.execSQL("DROP TABLE IF EXISTS favourite_words")
        db.execSQL("DROP TABLE IF EXISTS bookmarks")
        db.execSQL("DROP TABLE IF EXISTS word_details")
    }

    private fun executeSqlScript(db: SQLiteDatabase, scriptPath: String) {
        try {
            if (DevConfig.LOG_DATABASE_OPERATIONS) {
                Log.d(TAG, "Executing script: $scriptPath")
            }

            appContext.assets.open(scriptPath).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val sql = StringBuilder()
                    reader.forEachLine { line ->
                        // Skip comments and empty lines
                        val trimmedLine = line.trim()
                        if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("--")) {
                            sql.append(trimmedLine)
                            if (trimmedLine.endsWith(";")) {
                                val sqlStatement = sql.toString()
                                if (DevConfig.LOG_DATABASE_OPERATIONS) {
                                    Log.d(TAG, "Executing SQL: $sqlStatement")
                                }
                                try {
                                    db.execSQL(sqlStatement)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error executing SQL: $sqlStatement", e)
                                }
                                sql.clear()
                            } else {
                                sql.append(" ")
                            }
                        }
                    }
                }
            }
            if (DevConfig.LOG_DATABASE_OPERATIONS) {
                Log.d(TAG, "Successfully executed script: $scriptPath")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading SQL file: $scriptPath", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error executing SQL script: $scriptPath", e)
        }
    }

    // Helper method to check if database has data
    fun isDatabasePopulated(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='books'", null)
        val hasBooksTable = cursor.count > 0
        cursor.close()

        if (hasBooksTable) {
            val countCursor = db.rawQuery("SELECT COUNT(*) FROM books", null)
            countCursor.moveToFirst()
            val count = countCursor.getInt(0)
            countCursor.close()
            db.close()
            return count > 0
        }
        db.close()
        return false
    }

    // Clear all data but keep tables
    fun clearAllData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM books")
        db.execSQL("DELETE FROM exercises")
        db.execSQL("DELETE FROM favourite_words")
        db.execSQL("DELETE FROM bookmarks")
        db.close()
        Log.d(TAG, "All data cleared from database")
    }
}