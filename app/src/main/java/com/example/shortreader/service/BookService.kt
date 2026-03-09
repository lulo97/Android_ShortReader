package com.example.shortreader.service

import android.content.Context
import com.example.shortreader.models.Book
import com.example.shortreader.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookService(private val context: Context) {
    private val repository = BookRepository(context)

    fun getAllBooks(): Flow<List<Book>> = flow {
        emit(repository.getAllBooks())
    }.flowOn(Dispatchers.IO)

    fun getBookById(id: Int): Flow<Book?> = flow {
        emit(repository.getBookById(id))
    }.flowOn(Dispatchers.IO)

    fun searchBooks(query: String): Flow<List<Book>> = flow {
        emit(repository.searchBooks(query))
    }.flowOn(Dispatchers.IO)
}