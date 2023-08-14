package com.je.playground.databaseV2


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseProgramV2Dao
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseProgramWithExercisesV2Dao
import com.je.playground.databaseV2.exerciseprogram.dao.ExerciseV2Dao
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseProgramV2
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseV2
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
        WeekdaySchedule::class,
        ExerciseProgramV2::class,
        ExerciseV2::class,
        TaskGroup::class,
        TaskV2::class
    ],
    version = 1,
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
    abstract fun getExerciseProgramWithExercisesV2Dao() : ExerciseProgramWithExercisesV2Dao
    abstract fun getExerciseProgramV2Dao() : ExerciseProgramV2Dao
    abstract fun getExerciseV2Dao() : ExerciseV2Dao
    abstract fun getTaskGroupDao() : TaskGroupDao
    abstract fun getTaskV2Dao() : TaskV2Dao

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