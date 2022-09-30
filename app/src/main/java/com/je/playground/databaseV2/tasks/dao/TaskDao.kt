package com.je.playground.databaseV2.tasks.dao

import androidx.room.*
import com.je.playground.databaseV2.tasks.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("select * from task")
    fun getAll() : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task : Task) : Long

    @Delete
    suspend fun delete(task : Task)
}