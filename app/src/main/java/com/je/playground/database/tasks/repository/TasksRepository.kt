package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.dao.TaskDao
import com.je.playground.database.tasks.dao.TaskGroupDao
import com.je.playground.database.tasks.dao.TaskGroupWithTasksDao
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskGroup
import com.je.playground.database.tasks.entity.TaskGroupWithTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TasksRepository @Inject constructor(
    private val taskGroupWithTasksDao : TaskGroupWithTasksDao,
    private val taskGroupDao : TaskGroupDao,
    private val taskDao : TaskDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertTask(task : Task) : Long = withContext(Dispatchers.IO) {
        taskDao.insert(task)
    }

    fun deleteTask(task : Task) = coroutineScope.launch { taskDao.delete(task) }

    fun getAllTasks() : Flow<List<Task>> = taskDao.getAll()

    suspend fun insertTaskGroup(taskGroup : TaskGroup) : Long = withContext(Dispatchers.IO) {
        taskGroupDao.insertTaskGroup(taskGroup)
    }

    fun getAllTaskGroups() : Flow<List<TaskGroup>> = taskGroupDao.getAll()

    fun getAllTaskGroupsWithTasks() : Flow<List<TaskGroupWithTasks>> = taskGroupWithTasksDao.getAll()
}