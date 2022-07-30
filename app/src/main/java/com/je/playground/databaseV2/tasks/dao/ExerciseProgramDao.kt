package com.je.playground.databaseV2.tasks.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.je.playground.databaseV2.tasks.entity.ExerciseProgram

@Dao
interface ExerciseProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseProgram(vararg exerciseProgram : ExerciseProgram)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exerciseProgram : ExerciseProgram)
}