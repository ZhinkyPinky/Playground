package com.je.playground.databaseV2


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.je.playground.databaseV2.tasks.dao.*
import com.je.playground.databaseV2.tasks.entity.*

@Database(
    entities = [
        Exercise::class,
        ExerciseOccasion::class,
        ExerciseProgram::class,
        SimpleTask::class,
        Task::class,
        TaskOccasion::class,
        WeekdaySchedule::class
    ],
    version = 2,
    exportSchema = false //TODO: fix?
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabaseV2 : RoomDatabase() {
    abstract fun getExerciseDao() : ExerciseDao
    abstract fun getExerciseOccasionDao() : ExerciseOccasionDao
    abstract fun getExerciseProgramDao() : ExerciseProgramDao
    abstract fun getExerciseProgramWithExercisesDao() : ExerciseProgramWithExercisesDao
    abstract fun getSimpleTaskDao() : SimpleTaskDao
    abstract fun getTaskDao() : TaskDao
    abstract fun getTaskOccasionDao() : TaskOccasionDao
    abstract fun getTaskWithOccasionDao() : TaskWithOccasionsDao
    abstract fun getWeekdayScheduleDao() : WeekdayScheduleDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabaseV2? = null

        fun getDatabase(
            context : Context
        ) : AppDatabaseV2 {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context,
                        AppDatabaseV2::class.java,
                        "app_databaseV2"
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}