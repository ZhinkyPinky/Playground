package com.je.playground.database.exerciseprogram.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithAllTheThings
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramWithAllTheThingsDao {
    @Transaction
    @Query("select * from exercise_program")
    fun getAll() : Flow<List<ExerciseProgramWithAllTheThings>>

    @Transaction
    @Query("select * from exercise_program where exercise_program_id = :exerciseProgramId")
    fun selectById(exerciseProgramId : Long) : ExerciseProgramWithAllTheThings
}