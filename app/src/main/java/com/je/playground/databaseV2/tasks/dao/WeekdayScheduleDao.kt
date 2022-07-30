package com.je.playground.databaseV2.tasks.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule

@Dao
interface WeekdayScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weekdayScheduleEntry : WeekdaySchedule)

    @Delete
    suspend fun delete(weekdayScheduleEntry : WeekdaySchedule)
}