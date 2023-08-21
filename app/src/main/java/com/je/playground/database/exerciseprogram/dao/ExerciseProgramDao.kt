package com.je.playground.database.exerciseprogram.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram

@Dao
interface ExerciseProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseProgram(exerciseProgram : ExerciseProgram) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(exerciseProgram : ExerciseProgram)
}