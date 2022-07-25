package com.je.playground.database.tasks

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleTaskDao {
    @Query("select * from simple_task")
    fun getAllSimpleTasks() : Flow<List<SimpleTask>>
}