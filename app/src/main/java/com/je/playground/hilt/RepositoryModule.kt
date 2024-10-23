package com.je.playground.hilt

import com.je.playground.database.exerciseprogram.dao.ExerciseDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWeekdayScheduleDao
import com.je.playground.database.exerciseprogram.dao.ExerciseProgramWithAllTheThingsDao
import com.je.playground.database.exerciseprogram.repository.ExerciseProgramRepository
import com.je.playground.database.tasks.dao.*
import com.je.playground.database.tasks.repository.SubTaskRepository
import com.je.playground.database.tasks.repository.SubTaskRepositoryImpl
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepository
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun providesTasksRepository(
        taskDao : TaskDao,
        subTaskDao : SubTaskDao,
    ) : TaskRepository {
        return TaskRepository(
            taskDao = taskDao,
            subTaskDao = subTaskDao,
        )
    }


    @Provides
    @Singleton
    fun providesSubTaskRepository(
        subTaskDao : SubTaskDao
    ) : SubTaskRepository {
        return SubTaskRepositoryImpl(subTaskDao = subTaskDao)
    }

    @Provides
    @Singleton
    fun providesTaskWithSubTasksRepository(
        taskWithSubTasksDao : TaskWithSubTasksDao
    ) : TaskWithSubTasksRepository {
        return TaskWithSubTasksRepositoryImpl(taskWithSubTasksDao = taskWithSubTasksDao)
    }

    @Provides
    @Singleton
    fun providesExerciseProgramRepository(
        exerciseProgramWithAllTheThingsDao : ExerciseProgramWithAllTheThingsDao,
        exerciseProgramDao : ExerciseProgramDao,
        exerciseDao : ExerciseDao,
        exerciseProgramWeekdayScheduleDao : ExerciseProgramWeekdayScheduleDao
    ) : ExerciseProgramRepository {
        return ExerciseProgramRepository(
            exerciseProgramWithAllTheThingsDao = exerciseProgramWithAllTheThingsDao,
            exerciseProgramDao = exerciseProgramDao,
            exerciseDao = exerciseDao,
            exerciseProgramWeekdayScheduleDao = exerciseProgramWeekdayScheduleDao
        )
    }
}
