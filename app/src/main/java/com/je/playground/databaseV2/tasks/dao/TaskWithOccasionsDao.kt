package com.je.playground.databaseV2.tasks.dao

import androidx.room.Dao
import androidx.room.Query
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskWithOccasionsDao {
    @Query("select * from task left join exercise_program on exercise_program.exercise_program_id = task.task_id")
    fun getAll() : Flow<List<TaskWithOccasions>>
}