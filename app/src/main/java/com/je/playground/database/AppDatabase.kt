package com.je.playground.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithExercisesDao
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.tasks.dao.*
import com.je.playground.database.tasks.entity.*

@Database(
    entities = [
        SubTask::class,
        MainTask::class,
        Exercise::class,
        ExerciseProgram::class,
        WeekdaySchedule::class
    ],
    version = 1,
    exportSchema = false //TODO: fix?
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMainTaskWithSubTasksDao() : MainTaskWithSubTasksDao
    abstract fun getMainTaskDao() : MainTaskDao
    abstract fun getSubTaskDao() : SubTaskDao
    abstract fun getExerciseProgramWithExercisesDao() : ExerciseProgramWithExercisesDao
    abstract fun getExerciseProgramDao() : ExerciseProgramDao
    abstract fun getExerciseDao() : ExerciseDao
    abstract fun getWeekdayScheduleDao() : WeekdayScheduleDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(
            context : Context
        ) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}