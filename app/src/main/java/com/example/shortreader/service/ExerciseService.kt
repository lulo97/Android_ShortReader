package com.example.shortreader.service

import android.content.Context
import com.example.shortreader.models.Exercise
import com.example.shortreader.repository.ExerciseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ExerciseService(private val context: Context) {
    private val repository = ExerciseRepository(context)

    fun getAllExercises(): Flow<List<Exercise>> = flow {
        emit(repository.getAllExercises())
    }.flowOn(Dispatchers.IO)

    fun getExercisesByDifficulty(difficulty: String): Flow<List<Exercise>> = flow {
        emit(repository.getExercisesByDifficulty(difficulty))
    }.flowOn(Dispatchers.IO)
}