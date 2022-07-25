package com.je.playground.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.je.playground.database.tasks.*

@Database(
    entities = [
        Exercise::class,
        ExerciseProgram::class,
        ExerciseProgramExerciseConnection::class,
        SimpleTask::class
    ],
    version = 2
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSimpleTaskDao() : SimpleTaskDao

    abstract fun getExerciseDao() : ExerciseDao

    abstract fun getExerciseProgramDao() : ExerciseProgramDao

    abstract fun getExerciseProgramExerciseConnectionDao() : ExerciseProgramExerciseConnectionDao

    abstract fun getExerciseProgramWithExercisesDao() : ExerciseProgramWithExercisesDao

    abstract fun getExerciseProgramWithExercisesAndConnectionDao() : ExerciseProgramWithExercisesAndConnectionDao


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