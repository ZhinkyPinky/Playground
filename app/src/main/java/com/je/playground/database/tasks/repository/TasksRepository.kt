package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.dao.MainTaskDao
import com.je.playground.database.tasks.dao.MainTaskWithSubTasksDao
import com.je.playground.database.tasks.dao.SubTaskDao
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.entity.SubTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TasksRepository @Inject constructor(
    private val mainTaskWithSubTasksDao : MainTaskWithSubTasksDao,
    private val mainTaskDao : MainTaskDao,
    private val subTaskDao : SubTaskDao
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun insertSubTask(subTask : SubTask) : Long = withContext(Dispatchers.IO) {
        subTaskDao.insert(subTask)
    }

    fun deleteSubTask(subTask : SubTask) = coroutineScope.launch { subTaskDao.delete(subTask) }

    fun getAllSubTasks() : Flow<List<SubTask>> = subTaskDao.getAll()

    suspend fun insertMainTask(mainTask : MainTask) : Long = withContext(Dispatchers.IO) {
        mainTaskDao.insertMainTask(mainTask)
    }

    fun getAllMainTasks() : Flow<List<MainTask>> = mainTaskDao.getAll()

    fun getAllMainTasksWithSubTasks() : Flow<List<MainTaskWithSubTasks>> = mainTaskWithSubTasksDao.getAll()

    fun selectMainTaskWithSubTasksByMainTaskId(mainTaskId : Long) : MainTaskWithSubTasks? =  mainTaskWithSubTasksDao.selectFirstById(mainTaskId)
}