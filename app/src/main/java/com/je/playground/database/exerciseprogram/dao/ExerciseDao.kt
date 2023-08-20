package com.je.playground.database.exerciseprogram.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.je.playground.database.exerciseprogram.entity.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise : Exercise) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExercise(exercise : Exercise)

    @Delete
    fun delete(exercise : Exercise)
}