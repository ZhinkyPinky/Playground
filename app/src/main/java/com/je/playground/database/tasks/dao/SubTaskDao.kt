package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.je.playground.database.tasks.entity.SubTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subTask: SubTask): Long

    @Query("SELECT * FROM sub_task WHERE sub_task_id = :id")
    suspend fun getById(id: Long): SubTask?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(subTask: SubTask)

    @Query("select * from sub_task")
    fun getAll(): Flow<List<SubTask>>

    @Query("SELECT * FROM sub_task WHERE task_id = :taskId")
    fun getAllByTaskId(taskId: Long): Flow<List<SubTask>>

    @Delete
    suspend fun delete(subTask: SubTask): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSubTasks(subTasks: List<SubTask>)


}