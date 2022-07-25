package com.je.playground.database.tasks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseProgram(vararg exerciseProgram : ExerciseProgram)
}