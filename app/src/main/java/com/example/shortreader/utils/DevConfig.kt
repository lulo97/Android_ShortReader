package com.example.shortreader.utils

object DevConfig {
    // Set this to true for development to recreate database on each app start
    // Set to false for production to preserve user data
    const val DROP_DATABASE_ON_START = true  // Change to false for production

    // Optional: Clear shared preferences as well
    const val CLEAR_SHARED_PREFS = true

    // Log database operations
    const val LOG_DATABASE_OPERATIONS = true
}