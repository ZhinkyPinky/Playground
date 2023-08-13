package com.je.playground.ui.exerciseprogram

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseProgramWithExercisesV2
import com.je.playground.databaseV2.exerciseprogram.repository.ExerciseProgramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseProgramUiState(
    val exerciseProgramWithExercisesV2 : ExerciseProgramWithExercisesV2? = null
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
            combine(
                exerciseProgramRepository.getExerciseWithExercises(),
            ) { exerciseProgramWithExercisesV2 ->
                ExerciseProgramUiState(
                    exerciseProgramWithExercisesV2 = exerciseProgramWithExercisesV2[0][0]
                )
            }
                .catch { throwable ->
                    throwable.printStackTrace()
                }
                .collect {
                    _exerciseProgramUiState.value = it
                }
        }
    }
}