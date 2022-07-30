package com.je.playground.databaseV2.tasks.dao

import androidx.room.*
import com.je.playground.databaseV2.tasks.entity.Exercise
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion

@Dao
interface ExerciseOccasionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exerciseOccasion : ExerciseOccasion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exerciseOccasion : ExerciseOccasion)

    @Delete
    suspend fun delete(exerciseOccasion : ExerciseOccasion)
}