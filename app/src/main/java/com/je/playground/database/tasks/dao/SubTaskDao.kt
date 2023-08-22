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
    suspend fun insert(subTask : SubTask) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(subTask : SubTask)

    @Query("select * from sub_task")
    fun getAll() : Flow<List<SubTask>>

    @Delete
    fun delete(subTask : SubTask)
}