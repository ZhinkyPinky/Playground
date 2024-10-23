package com.je.playground.hilt

import com.je.playground.database.AppDatabase
import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWeekdayScheduleDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithAllTheThingsDao
import com.je.playground.database.tasks.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideMainTaskWithSubTasksDao(appDatabase : AppDatabase) : TaskWithSubTasksDao {
        return appDatabase.getTaskWithSubTasksDao()
    }

    @Provides
    @Singleton
    fun provideMainTaskDao(appDatabase : AppDatabase) : TaskDao {
        return appDatabase.getTaskDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase : AppDatabase) : SubTaskDao {
        return appDatabase.getSubTaskDao()
    }

    @Provides
    @Singleton
    fun provideExerciseProgramWithExercisesDao(appDatabase : AppDatabase) : ExerciseProgramWithAllTheThingsDao {
        return appDatabase.getExerciseProgramWithExercisesDao()
    }

    @Provides
    @Singleton
    fun provideExerciseProgramDao(appDatabase : AppDatabase) : ExerciseProgramDao {
        return appDatabase.getExerciseProgramDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(appDatabase : AppDatabase) : ExerciseDao {
        return appDatabase.getExerciseDao()
    }

    @Provides
    @Singleton
    fun provideExerciseProgramWeekdayScheduleDao(appDatabase : AppDatabase) : ExerciseProgramWeekdayScheduleDao {
        return appDatabase.getExerciseProgramWeekdayScheduleDao()
    }
}