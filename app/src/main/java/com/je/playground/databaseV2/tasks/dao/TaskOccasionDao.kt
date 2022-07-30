package com.je.playground.databaseV2.tasks.dao

import androidx.room.*
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface TaskOccasionDao {
    @Query("select * from task_occasion")
    fun getAll() : Flow<List<TaskOccasion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskOccasion : TaskOccasion) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(taskOccasion : TaskOccasion)

    @Delete
    suspend fun delete(taskOccasion : TaskOccasion)

    @Query("delete from task_occasion where task_id = :taskId and date_from = :dateFrom")
    suspend fun deleteWithTaskIdAndDateTimeFrom(
        taskId : Long,
        dateFrom : LocalDate
    )
}