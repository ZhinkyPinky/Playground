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
    @Query("select * from task")
    fun getAll() : Flow<List<Task>>

    @Query("SELECT * FROM task WHERE task_id = :id")
    fun getTaskById(id : Long) : Flow<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : Task) : Long

    //@Query("UPDATE task SET is_completed = (SELECT MIN(is_completed) FROM sub_task WHERE task_id = :taskId AND is_completed <> task.is_completed) WHERE task_id = :taskId")
    @Query("UPDATE task SET is_completed = (SELECT CASE WHEN SUM(CASE WHEN is_completed = 1 THEN 1 else 0 END) = COUNT(*) THEN 1 ELSE 0 END FROM sub_task WHERE task_id = :taskId) WHERE task_id = :taskId")
    suspend fun toggleCompletionBasedOnSubTasksCompletion(taskId : Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task : Task)

    @Delete
    suspend fun deleteTask(task : Task)
}