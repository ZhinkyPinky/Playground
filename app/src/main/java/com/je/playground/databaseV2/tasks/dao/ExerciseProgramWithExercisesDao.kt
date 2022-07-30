package com.je.playground.databaseV2.tasks.dao

import androidx.room.Dao
import androidx.room.Query
import com.je.playground.databaseV2.tasks.entity.ExerciseProgramWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramWithExercisesDao {
    @Query("select * from exercise_program")
    fun getAll() : Flow<List<ExerciseProgramWithExercises>>
}