package com.je.playground.database.tasks

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramWithExercisesAndConnectionDao {
    @Query("select * from exercise_program")
    fun getAll() : Flow<List<ExerciseProgramWithExercisesAndConnections>>
}