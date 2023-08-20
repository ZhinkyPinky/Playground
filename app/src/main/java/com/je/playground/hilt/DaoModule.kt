package com.je.playground.hilt

import com.je.playground.database.AppDatabase
import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithExercisesDao
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
    fun provideTaskDao(appDatabase : AppDatabase) : TaskDao {
        return appDatabase.getTaskDao()
    }

    @Provides
    @Singleton
    fun provideTaskGroupDao(appDatabase : AppDatabase) : TaskGroupDao {
        return appDatabase.getTaskGroupDao()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(appDatabase : AppDatabase) : ExerciseDao {
        return appDatabase.getExerciseDao()
    }

    @Provides
    @Singleton
    fun bindExerciseProgramDao(appDatabase : AppDatabase) : ExerciseProgramDao {
        return appDatabase.getExerciseProgramDao()
    }

    @Provides
    @Singleton
    fun bindExerciseProgramWithExercisesDao(appDatabase : AppDatabase) : ExerciseProgramWithExercisesDao {
        return appDatabase.getExerciseProgramWithExercisesDao()
    }

    @Provides
    @Singleton
    fun bindWeekdayScheduleDao(appDatabase : AppDatabase) : WeekdayScheduleDao {
        return appDatabase.getWeekdayScheduleDao()
    }
}