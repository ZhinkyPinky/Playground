package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.je.playground.database.tasks.entity.TaskGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskGroupDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertTaskGroup(taskGroup : TaskGroup) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTaskGroup(taskGroup : TaskGroup)

    @Delete
    fun deleteTaskGroup(taskGroup : TaskGroup)

    @Query("select * from task_group")
    fun getAll() : Flow<List<TaskGroup>>
}