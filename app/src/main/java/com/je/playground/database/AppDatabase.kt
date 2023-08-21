package com.je.playground.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithExercisesDao
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.tasks.dao.*
import com.je.playground.database.tasks.entity.*

@Database(
    entities = [
        Task::class,
        TaskGroup::class,
        Exercise::class,
        ExerciseProgram::class,
        WeekdaySchedule::class
    ],
    version = 2,
    exportSchema = false //TODO: fix?
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskDao() : TaskDao
    abstract fun getTaskGroupDao() : TaskGroupDao
    abstract fun getTaskGroupWithTasksDao() : TaskGroupWithTasksDao
    abstract fun getExerciseDao() : ExerciseDao
    abstract fun getExerciseProgramDao() : ExerciseProgramDao
    abstract fun getExerciseProgramWithExercisesDao() : ExerciseProgramWithExercisesDao
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
                        "app_databaseV2"
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(Callback())

                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

    open class Callback : RoomDatabase.Callback() {
        override fun onCreate(db : SupportSQLiteDatabase) {
            super.onCreate(db)

            db.beginTransaction()
            db.endTransaction()
        }
    }
}