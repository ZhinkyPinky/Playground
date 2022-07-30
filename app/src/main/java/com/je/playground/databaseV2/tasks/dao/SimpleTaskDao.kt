package com.je.playground.databaseV2.tasks.dao

import androidx.room.*
import com.je.playground.databaseV2.tasks.entity.SimpleTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleTaskDao {
    @Query("select * from simple_task")
    fun getAll() : Flow<List<SimpleTask>>

    @Query("select * from simple_task where simple_task_id = :id")
    fun getSimpleTaskWithId(id : Int) : Flow<List<SimpleTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimpleTask(simpleTask : SimpleTask) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(simpleTask : SimpleTask) : Int
}