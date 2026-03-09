package com.example.shortreader.models

data class Book(
    val title: String,
    val wordCount: Int,
    val preview: String,
    val fullText: String
)