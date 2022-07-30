package com.je.playground.databaseV2.tasks.dao

import androidx.room.*
import com.je.playground.databaseV2.tasks.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("select * from exercise")
    fun getAllExercises() : Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(vararg exercise : Exercise)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExercise(exercise : Exercise)

    @Delete
    suspend fun delete(exercise : Exercise)
}