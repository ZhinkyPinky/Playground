package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.entity.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow

interface TaskWithSubTasksRepository {
    fun getByTaskId(taskId: Long): Flow<TaskWithSubTasks>

    fun getAll(): Flow<List<TaskWithSubTasks>>

    fun getAllActive(): Flow<List<TaskWithSubTasks>>

    fun getAllArchived(): Flow<List<TaskWithSubTasks>>
}