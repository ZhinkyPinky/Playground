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
    @Query("select * from main_task")
    fun getAll() : Flow<List<Task>>

    @Query("SELECT * FROM main_task WHERE main_task_id = :id")
    fun getTaskById(id : Long) : Flow<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainTask(task : Task) : Long

    //@Query("UPDATE main_task SET is_completed = (SELECT CASE WHEN MAX(is_completed) = MIN(is_completed) THEN MIN(is_completed)  ELSE 0 END FROM sub_task WHERE main_task_id = :taskId)")
    //@Query("UPDATE main_task SET is_completed = IF S")
    //suspend fun thing(taskId:Long)


    //@Query("UPDATE main_task SET is_completed = (SELECT MIN(is_completed) FROM sub_task WHERE main_task_id = :taskId AND MIN(is_completed) <> main_task.is_completed)")
    @Query("UPDATE main_task SET is_completed = (SELECT CASE WHEN SUM(CASE WHEN is_completed = 1 THEN 1 else 0 END) = COUNT(*) THEN 1 ELSE 0 END FROM sub_task WHERE main_task_id = :taskId) WHERE main_task_id = :taskId")
    suspend fun toggleCompletionBasedOnSubTasksCompletion(taskId : Long)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMainTask(task : Task)

    @Delete
    suspend fun deleteMainTask(task : Task)
}