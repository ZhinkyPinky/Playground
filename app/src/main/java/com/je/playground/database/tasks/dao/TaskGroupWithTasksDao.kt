package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.je.playground.database.tasks.entity.TaskGroupWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskGroupWithTasksDao {
    @Transaction
    @Query("select * from task_group")
    fun getAll() : Flow<List<TaskGroupWithTasks>>
}