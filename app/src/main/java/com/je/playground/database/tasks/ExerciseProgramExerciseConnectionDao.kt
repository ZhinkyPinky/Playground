package com.je.playground.database.tasks

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseProgramExerciseConnectionDao {
    @Query("select * from exercise_program_exercise_connection")
    fun getAllConnections() : Flow<List<ExerciseProgramExerciseConnection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExerciseProgramExerciseConnection(vararg exerciseProgramExerciseConnection : ExerciseProgramExerciseConnection)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(exerciseProgramExerciseConnection : ExerciseProgramExerciseConnection)
}