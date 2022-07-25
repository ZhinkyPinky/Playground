package com.je.playground.database.tasks

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("select * from exercise")
    fun getAllExercises() : Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(vararg exercise : Exercise)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExercise(exercise : Exercise)
}