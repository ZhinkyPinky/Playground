package com.je.playground.view.exerciseprogramview.exerciseprogrameditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.database.exerciseprogram.entity.Exercise
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithAllTheThings
import com.je.playground.view.exerciseprogramview.exerciseprogrameditor.dialog.ExerciseProgramEditorDialog
import com.je.playground.view.exerciseprogramview.viewmodel.ExerciseProgramViewModel
import com.je.playground.view.taskview.taskeditor.EditorTopBar
import com.je.playground.view.theme.title

@Composable
fun ExerciseProgramEditorScreen(
    exerciseProgramId : Long?,
    exerciseProgramViewModel : ExerciseProgramViewModel,
    onBackPress : () -> Unit,
) {
    var exerciseProgramWithAllTheThings = ExerciseProgramWithAllTheThings(exerciseProgram = ExerciseProgram())
    var exerciseProgram = exerciseProgramWithAllTheThings.exerciseProgram
    var exerciseProgramTitle by rememberSaveable { mutableStateOf(exerciseProgram.title) }
    var exerciseProgramTaskPriority by rememberSaveable { mutableStateOf(exerciseProgram.priority) }
    var exerciseProgramIsActive by rememberSaveable { mutableStateOf(exerciseProgram.isActive) }

    val exercises = rememberSaveable(
        saver = listSaver(
            save = {
                if (it.isNotEmpty()) {
                    val first = it.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }

                it.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        exerciseProgramWithAllTheThings.exercises
            .map { it.copy() }
            .toMutableStateList()
    }

    val weekdaySchedule = rememberSaveable(
        saver = listSaver(
            save = {
                if (it.isNotEmpty()) {
                    val first = it.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }

                it.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        exerciseProgramWithAllTheThings.weekdaySchedule
            .map { it.copy() }
            .toMutableStateList()
    }

    var showExerciseProgramEditorDialog by rememberSaveable { mutableStateOf(false) }

    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(
        Unit
    ) {
        exerciseProgramId?.let { id ->
            exerciseProgramViewModel
                .selectExerciseProgramWithAllTheThingsById(id)
                .let {
                    exerciseProgramWithAllTheThings = it
                    exerciseProgram = exerciseProgramWithAllTheThings.exerciseProgram
                    exerciseProgramTitle = exerciseProgram.title
                    exerciseProgramTaskPriority = exerciseProgram.priority
                    exerciseProgramIsActive = exerciseProgram.isActive

                    exerciseProgramWithAllTheThings.exercises.forEach { exercise ->
                        exercises.add(exercise)
                    }

                    exerciseProgramWithAllTheThings.weekdaySchedule.forEach { entry ->
                        weekdaySchedule.add(entry)
                    }
                }
        }

        isLoading = false
    }

    if (!isLoading) {
        ExerciseProgramEditorContent(
            saveExerciseProgramWithAllTheThings = {
                exerciseProgram.title = exerciseProgramTitle
                exerciseProgram.priority = exerciseProgramTaskPriority
                exerciseProgram.isActive = exerciseProgramIsActive

                exerciseProgramViewModel.saveExerciseProgramWithAllTheThings(
                    exerciseProgramWithAllTheThings = ExerciseProgramWithAllTheThings(
                        exerciseProgram = exerciseProgram,
                        exercises = exercises,
                        weekdaySchedule = weekdaySchedule
                    )
                )

                onBackPress()
            },
            exerciseProgramId = exerciseProgram.exerciseProgramId,
            exerciseProgramTitle = exerciseProgramTitle,
            updateExerciseProgramTitle = {
                exerciseProgramTitle = it
                exerciseProgram.title = it
            },
            exerciseProgramTaskPriority = exerciseProgramTaskPriority,
            updateExerciseProgramTaskPriority = {
                exerciseProgramTaskPriority = it
                exerciseProgram.priority = it
            },
            exerciseProgramIsActive = exerciseProgramIsActive,
            toggleIsActive = {
                exerciseProgramIsActive = !exerciseProgramIsActive
                exerciseProgram.isActive = exerciseProgramIsActive
            },
            updateExerciseProgram = { title, priority, isActive ->
                exerciseProgramTitle = title
                exerciseProgram.title = title

                exerciseProgramTaskPriority = priority
                exerciseProgram.priority = priority

                exerciseProgramIsActive = isActive
                exerciseProgramIsActive = isActive
            },
            exercises = exercises,
            showExerciseProgramEditorDialog = showExerciseProgramEditorDialog,
            toggleExerciseProgramEditorDialog = { showExerciseProgramEditorDialog = !showExerciseProgramEditorDialog },
            onBackPress = onBackPress
        )
    }
}

@Composable
fun ExerciseProgramEditorContent(
    saveExerciseProgramWithAllTheThings : () -> Unit,
    exerciseProgramId : Long,
    exerciseProgramTitle : String,
    updateExerciseProgramTitle : (String) -> Unit,
    exerciseProgramTaskPriority : Int,
    updateExerciseProgramTaskPriority : (Int) -> Unit,
    exerciseProgramIsActive : Boolean,
    toggleIsActive : () -> Unit,
    updateExerciseProgram : (String, Int, Boolean) -> Unit,
    exercises : SnapshotStateList<Exercise>,
    showExerciseProgramEditorDialog : Boolean,
    toggleExerciseProgramEditorDialog : () -> Unit,
    onBackPress : () -> Unit
) {
    if (showExerciseProgramEditorDialog) {
        ExerciseProgramEditorDialog(
            exerciseProgramId = exerciseProgramId,
            exerciseProgramTitle = exerciseProgramTitle,
            exerciseProgramTaskPriority = exerciseProgramTaskPriority,
            exerciseProgramIsActive = exerciseProgramIsActive,
            updateExerciseProgram = updateExerciseProgram,
            onDismissRequest = toggleExerciseProgramEditorDialog
        )
    }

    Scaffold(
        topBar = {
            EditorTopBar(
                text = "Edit",
                onSave = saveExerciseProgramWithAllTheThings,
                onBackPress
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            ExerciseProgramEditor(
                exerciseProgramId = exerciseProgramId,
                exerciseProgramTitle = exerciseProgramTitle,
                exerciseProgramTaskPriority = exerciseProgramTaskPriority,
                exerciseProgramIsActive = exerciseProgramIsActive,
                exercises = exercises,
                toggleExerciseProgramEditorDialog = toggleExerciseProgramEditorDialog,
            )

        }
    }
}

@Composable
fun ExerciseProgramEditor(
    exerciseProgramId : Long,
    exerciseProgramTitle : String,
    exerciseProgramTaskPriority : Int,
    exerciseProgramIsActive : Boolean,
    exercises : SnapshotStateList<Exercise>,
    toggleExerciseProgramEditorDialog : () -> Unit,
) {
    ExerciseProgramEditComponent(
        exerciseProgramId = exerciseProgramId,
        exerciseProgramTitle = exerciseProgramTitle,
        exerciseProgramTaskPriority = exerciseProgramTaskPriority,
        exerciseProgramIsActive = exerciseProgramIsActive,
        toggleExerciseProgramEditorDialog = toggleExerciseProgramEditorDialog,
    )

    /*
    SubTasksComponent(
        mainTaskId = mainTaskId,
        subTasks = subTasks,
    )
     */
}

@Composable
fun ExerciseProgramEditComponent(
    exerciseProgramId : Long,
    exerciseProgramTitle : String,
    exerciseProgramTaskPriority : Int,
    exerciseProgramIsActive : Boolean,
    toggleExerciseProgramEditorDialog : () -> Unit,
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 12.dp)
    ) {
        Text(
            text = "Group",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = toggleExerciseProgramEditorDialog
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit main task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(
                start = 6.dp,
                top = 6.dp,
                bottom = 6.dp
            )
    ) {
        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .background(
                    when (exerciseProgramTaskPriority) {
                        0 -> Color(0xFF00C853)
                        1 -> Color(0xFFFFAB00)
                        2 -> Color.Red
                        else -> Color.Transparent
                    }
                )
                .fillMaxHeight()
                .width(2.dp)
        )

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(
                    start = 6.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
        ) {
            Text(
                text = if (exerciseProgramTitle == "") "Title*" else exerciseProgramTitle,
                style = title(if (exerciseProgramTitle == "") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer),
                textAlign = TextAlign.Start
            )
        }
    }
}