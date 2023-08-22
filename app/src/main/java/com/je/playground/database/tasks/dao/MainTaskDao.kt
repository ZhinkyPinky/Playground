package com.je.playground.database.tasks.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.je.playground.database.tasks.entity.MainTask
import kotlinx.coroutines.flow.Flow

@Dao
interface MainTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMainTask(mainTask : MainTask) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMainTask(mainTask : MainTask)

    @Delete
    fun deleteMainTask(mainTask : MainTask)

    @Query("select * from main_task")
    fun getAll() : Flow<List<MainTask>>
}