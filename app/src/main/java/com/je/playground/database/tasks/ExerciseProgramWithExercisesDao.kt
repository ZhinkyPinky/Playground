package com.je.playground.database.tasks

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramWithExercisesDao {
    @Query("select * from exercise_program")
    fun getAllExerciseProgramsWithExercises() : Flow<List<ExerciseProgramWithExercises>>
}