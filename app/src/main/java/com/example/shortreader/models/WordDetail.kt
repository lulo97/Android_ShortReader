package com.example.shortreader.models

data class WordDetail(
    val word: String,
    val meaning: String,
    val example: String? = null,
    val partOfSpeech: String? = null,
    val pronunciation: String? = null
)