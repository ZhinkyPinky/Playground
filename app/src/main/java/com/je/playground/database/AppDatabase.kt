package com.je.playground.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWeekdayScheduleDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithAllTheThingsDao
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWeekdaySchedule
import com.je.playground.database.tasks.dao.*
import com.je.playground.database.tasks.entity.*

@Database(
    entities = [
        SubTask::class,
        MainTask::class,
        Exercise::class,
        ExerciseProgram::class,
        ExerciseProgramWeekdaySchedule::class
    ],
    version = 2,
    exportSchema = false //TODO: fix?
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMainTaskWithSubTasksDao() : MainTaskWithSubTasksDao
    abstract fun getMainTaskDao() : MainTaskDao
    abstract fun getSubTaskDao() : SubTaskDao
    abstract fun getExerciseProgramWithExercisesDao() : ExerciseProgramWithAllTheThingsDao
    abstract fun getExerciseProgramDao() : ExerciseProgramDao
    abstract fun getExerciseDao() : ExerciseDao
    abstract fun getExerciseProgramWeekdayScheduleDao() : ExerciseProgramWeekdayScheduleDao

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

/*
class RoomDbInitializer(
    private val exerciseProgramDaoProvider : Provider<ExerciseProgramDao>
) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db : SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.i(
            "dbCallback",
            "onCreate"
        )

        val exerciseProgramDao = exerciseProgramDaoProvider.get()
        val programId = exerciseProgramDao?.insertExerciseProgram(
            ExerciseProgram(
                id = 0L,
                name = "TestProgram",
                isActive = true
            )
        )
    }

    override fun onOpen(db : SupportSQLiteDatabase) {
        super.onOpen(db)
        Log.i(
            "dbCallback",
            "onOpen"
        )
    }

    override fun onDestructiveMigration(db : SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
        Log.i(
            "dbCallback",
            "onDestructiveMigration"
        )
    }
}
 */
