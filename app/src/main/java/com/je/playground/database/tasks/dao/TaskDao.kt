package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.je.playground.database.tasks.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(task : Task) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task : Task)

    @Query("select * from task")
    fun getAll() : Flow<List<Task>>

    @Delete
    fun delete(task : Task)
}