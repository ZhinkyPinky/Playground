package com.je.playground.feature.exerciseprogram.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithAllTheThings
import com.je.playground.database.exerciseprogram.repository.ExerciseProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ExerciseProgramUiState(
    val exerciseProgramsWithAllTheThings : List<ExerciseProgramWithAllTheThings> = emptyList()
)

@HiltViewModel
class ExerciseProgramViewModel @Inject constructor(
    private val application : Application,
    private val exerciseProgramRepository : ExerciseProgramRepository
) : ViewModel() {
    private val _exerciseProgramUiState = MutableStateFlow(ExerciseProgramUiState())

    val exerciseProgramUiState : StateFlow<ExerciseProgramUiState>
        get() = _exerciseProgramUiState

    init {
        viewModelScope.launch {
            exerciseProgramRepository
                .getAllExerciseProgramsWithAllTheThings()
                .collect { exerciseProgramsWithAllTheThings ->
                    _exerciseProgramUiState.update {
                        it.copy(exerciseProgramsWithAllTheThings = exerciseProgramsWithAllTheThings)
                    }
                }
        }
    }

    fun saveExerciseProgramWithAllTheThings(exerciseProgramWithAllTheThings : ExerciseProgramWithAllTheThings) {
        viewModelScope.launch {
            var exerciseProgramId = exerciseProgramRepository.insertExerciseProgram(exerciseProgramWithAllTheThings.exerciseProgram)

            val exercises = exerciseProgramWithAllTheThings.exercises
            exercises.forEach {
                exerciseProgramRepository.insertExercise(it)
            }

            val weekdaySchedule = exerciseProgramWithAllTheThings.weekdaySchedule
            weekdaySchedule.forEach {
                exerciseProgramRepository.insertExerciseProgramWeekdaySchedule(it)
            }
        }
    }

    suspend fun selectExerciseProgramWithAllTheThingsById(exerciseProgramId : Long) : ExerciseProgramWithAllTheThings = withContext(Dispatchers.IO) {
        exerciseProgramRepository.selectExerciseProgramWithAllTheThingsById(exerciseProgramId)
    }
}