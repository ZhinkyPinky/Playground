package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.dao.SubTaskDao
import com.je.playground.database.tasks.entity.SubTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubTaskRepositoryImpl @Inject constructor(
    private val subTaskDao : SubTaskDao
) : SubTaskRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override suspend fun insert(subTask : SubTask) : Long = subTaskDao.insert(subTask)
    override suspend fun getById(id : Long) : SubTask? = subTaskDao.getById(id)

    override suspend fun update(subTask : SubTask) = subTaskDao.update(subTask)

    override fun getAll() : Flow<List<SubTask>> = subTaskDao.getAll()

    override suspend fun getAllByTaskId(taskId : Long) : Flow<List<SubTask>> = subTaskDao.getAllByTaskId(taskId)

    override suspend fun delete(subTask : SubTask) = subTaskDao.delete(subTask)
}