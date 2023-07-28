package com.je.playground.databaseV2.tasks.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.je.playground.databaseV2.tasks.entity.ExerciseProgramWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramWithExercisesDao {
    @Transaction
    @Query("select * from exercise_program")
    fun getAll() : Flow<List<ExerciseProgramWithExercises>>
}