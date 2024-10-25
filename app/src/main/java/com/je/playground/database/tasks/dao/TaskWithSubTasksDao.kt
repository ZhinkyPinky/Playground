package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskWithSubTasksDao {
    @Transaction
    @Query("select * from main_task")
    fun getAll() : Flow<List<TaskWithSubTasks>>

    @Transaction
    @Query("select * from main_task where is_archived = 0")
    fun getAllActive() : Flow<List<TaskWithSubTasks>>

    @Transaction
    @Query("select * from main_task where is_archived = 1")
    fun getAllArchived() : Flow<List<TaskWithSubTasks>>

    @Transaction
    @Query("select * from main_task where main_task_id = :taskId")
    suspend fun getTaskWithSubTasksByTaskId(taskId : Long) : TaskWithSubTasks?
}