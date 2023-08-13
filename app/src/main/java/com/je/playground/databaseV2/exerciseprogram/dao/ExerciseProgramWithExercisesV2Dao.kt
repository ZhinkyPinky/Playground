package com.je.playground.databaseV2.exerciseprogram.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseProgramWithExercisesV2
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramWithExercisesV2Dao {
    @Transaction
    @Query("select * from exercise_program_v2")
    fun getAll() : Flow<List<ExerciseProgramWithExercisesV2>>
}