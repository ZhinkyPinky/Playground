package com.je.playground.database.tasks.repository

import com.je.playground.database.tasks.dao.SubTaskDao
import com.je.playground.database.tasks.entity.SubTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubTaskRepositoryImpl @Inject constructor(
    private val subTaskDao: SubTaskDao
) : SubTaskRepository {

    override suspend fun insert(subTask: SubTask): Long = subTaskDao.insert(subTask)

    override suspend fun getById(subTaskId: Long): SubTask? = subTaskDao.getById(subTaskId)

    override suspend fun update(subTask: SubTask) = subTaskDao.update(subTask)

    override fun getAll(): Flow<List<SubTask>> = subTaskDao.getAll()

    override fun getAllByTaskId(taskId: Long): Flow<List<SubTask>> =
        subTaskDao.getAllByTaskId(taskId)

    override suspend fun delete(subTask: SubTask): Int = subTaskDao.delete(subTask)

}