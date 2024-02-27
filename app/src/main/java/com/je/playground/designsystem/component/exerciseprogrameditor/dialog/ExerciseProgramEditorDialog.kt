package com.je.playground.designsystem.component.exerciseprogrameditor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.je.playground.designsystem.component.PrioritySliderComponent
import com.je.playground.designsystem.component.TextFieldComponent
import com.je.playground.designsystem.component.schedule.WeekComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseProgramEditorDialog(
    exerciseProgramId : Long,
    exerciseProgramTitle : String,
    exerciseProgramTaskPriority : Int,
    exerciseProgramIsActive : Boolean,
    updateExerciseProgram : (String, Int, Boolean) -> Unit,
    onDismissRequest : () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf(exerciseProgramTitle) }
    var priority by rememberSaveable { mutableIntStateOf(exerciseProgramTaskPriority) }
    var isActive by rememberSaveable { mutableStateOf(exerciseProgramIsActive) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.padding(24.dp)
    ) {
        Surface(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .wrapContentHeight()
                .wrapContentWidth()
        ) {
            Column {
                Text(
                    text = "Edit exercise Program",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 12.dp
                        )
                )

                Divider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 6.dp
                    )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp
                    )
                ) {
                    TextFieldComponent(
                        label = "Name*",
                        placeholder = "Enter a name for the program",
                        value = title,
                        isSingleLine = true,
                        onValueChange = { title = it }
                    )

                    WeekComponent(
                        selectedWeekDays = emptyList(),
                        insertWeekdayScheduleEntry = {},
                        deleteWeekdayScheduleEntry = {}
                    )

                    PrioritySliderComponent(
                        priority = priority,
                        onPriorityChanged = { priority = it },
                        modifier = Modifier.padding(
                            bottom = 6.dp
                        ),
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    ) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            updateExerciseProgram(
                                title,
                                priority,
                                isActive
                            )
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}