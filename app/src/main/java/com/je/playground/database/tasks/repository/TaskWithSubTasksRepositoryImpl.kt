package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.dao.TaskWithSubTasksDao
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskWithSubTasksRepositoryImpl @Inject constructor(
    private val taskWithSubTasksDao : TaskWithSubTasksDao
) : TaskWithSubTasksRepository {
    override fun getAll() : Flow<List<TaskWithSubTasks>> = taskWithSubTasksDao.getAll()

    override fun getAllActive() : Flow<List<TaskWithSubTasks>> = taskWithSubTasksDao.getAllActive()

    override suspend fun getByTaskId(taskId : Long) : TaskWithSubTasks? = taskWithSubTasksDao.getTaskWithSubTasksByTaskId(taskId)

    override fun getAllArchived() : Flow<List<TaskWithSubTasks>> = taskWithSubTasksDao.getAllArchived()
}