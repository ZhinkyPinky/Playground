package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.entity.SubTask
import kotlinx.coroutines.flow.Flow

interface SubTaskRepository {
    suspend fun insert(subTask : SubTask) : Long

    suspend fun getById(subTaskId : Long) : SubTask?

    suspend fun update(subTask : SubTask)

    suspend fun delete(subTask : SubTask): Int

    fun getAll() : Flow<List<SubTask>>

    fun getAllByTaskId(taskId : Long) : Flow<List<SubTask>>
}