package com.je.playground.database.exerciseprogram.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWeekdaySchedule

@Dao
interface ExerciseProgramWeekdayScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(exerciseProgramWeekdaySchedule : ExerciseProgramWeekdaySchedule)
}