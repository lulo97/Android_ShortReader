package com.example.shortreader.service

import android.content.Context
import com.example.shortreader.models.FavouriteItem
import com.example.shortreader.repository.FavouriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavouriteService(private val context: Context) {
    private val repository = FavouriteRepository(context)

    fun getAllFavourites(): Flow<List<FavouriteItem>> = flow {
        emit(repository.getAllFavourites())
    }.flowOn(Dispatchers.IO)

    fun addFavourite(item: FavouriteItem): Flow<Boolean> = flow {
        emit(repository.addFavourite(item))
    }.flowOn(Dispatchers.IO)

    fun deleteFavourite(word: String): Flow<Int> = flow {
        emit(repository.deleteFavourite(word))
    }.flowOn(Dispatchers.IO)

    fun searchFavourites(query: String): Flow<List<FavouriteItem>> = flow {
        emit(repository.searchFavourites(query))
    }.flowOn(Dispatchers.IO)
}