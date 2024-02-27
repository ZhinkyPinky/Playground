package com.je.playground.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWeekdayScheduleDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithAllTheThingsDao
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWeekdaySchedule
import com.je.playground.database.tasks.dao.SubTaskDao
import com.je.playground.database.tasks.dao.TaskDao
import com.je.playground.database.tasks.dao.TaskWithSubTasksDao
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task

@Database(
    entities = [
        SubTask::class,
        Task::class,
        Exercise::class,
        ExerciseProgram::class,
        ExerciseProgramWeekdaySchedule::class
    ],
    version = 2,
    exportSchema = false //TODO: fix?
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskWithSubTasksDao() : TaskWithSubTasksDao
    abstract fun getTaskDao() : TaskDao
    abstract fun getSubTaskDao() : SubTaskDao
    abstract fun getExerciseProgramWithExercisesDao() : ExerciseProgramWithAllTheThingsDao
    abstract fun getExerciseProgramDao() : ExerciseProgramDao
    abstract fun getExerciseDao() : ExerciseDao
    abstract fun getExerciseProgramWeekdayScheduleDao() : ExerciseProgramWeekdayScheduleDao
}
