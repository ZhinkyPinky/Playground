package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface MainTaskWithSubTasksDao {
    @Transaction
    @Query("select * from main_task")
    fun getAll() : Flow<List<MainTaskWithSubTasks>>

    @Query("select * from main_task where main_task_id = :mainTaskId")
    fun selectFirstById(mainTaskId : Long) : MainTaskWithSubTasks?
}