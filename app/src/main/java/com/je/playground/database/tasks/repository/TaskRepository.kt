package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.dao.SubTaskDao
import com.je.playground.database.tasks.dao.TaskDao
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertTask(task: Task): Long =
        withContext(Dispatchers.IO) { taskDao.insertTask(task) }

    fun updateTask(task: Task) = coroutineScope.launch { taskDao.updateTask(task) }

    fun deleteTask(task: Task) = coroutineScope.launch { taskDao.deleteTask(task) }

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAll()

    suspend fun insertSubTask(subTask: SubTask): Long =
        withContext(Dispatchers.IO) { subTaskDao.insert(subTask) }

    fun updateSubTasks(subTasks: List<SubTask>) =
        coroutineScope.launch { subTaskDao.updateSubTasks(subTasks) }

    suspend fun toggleCompletionBasedOnSubTasksCompletion(taskId: Long) =
        taskDao.toggleCompletionBasedOnSubTasksCompletion(taskId)

    fun getAllSubTasks(): Flow<List<SubTask>> = subTaskDao.getAll()

    fun getTaskById(id: Long): Flow<Task> {
        return taskDao.getTaskById(id)
    }
}