package com.je.playground.ui.tasklist.components.deprecated.exercise

import android.os.CountDownTimer
import android.os.VibrationEffect
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.Exercise

@Composable fun ExerciseComponent(exercise : Exercise) {
    Spacer(modifier = Modifier.height(1.dp))

    Column {

        SetsComponent(exercise = exercise)

        RepsComponent(exercise = exercise)

        RestComponent(exercise = exercise)

        Divider(
            color = MaterialTheme.colors.primaryVariant,
        )
    }
}

@Composable
fun SetsComponent(exercise : Exercise) {
    Text(
        text = "Sets:  ${exercise.sets}",
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 8.dp,
            bottom = 8.dp
        )
    )
}

@Composable
fun RepsComponent(exercise : Exercise) {
    Text(
        text = "Reps: ${exercise.reps}",
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 8.dp,
            bottom = 8.dp
        )
    )
}

@Composable
fun RestComponent(exercise : Exercise) {
    Row {
        var isTimerOn by remember { mutableStateOf(false) }
        var restTime by remember {
            mutableStateOf(exercise.restTime.seconds)
        }

        Text(
            text = "Rest: ${restTime / 1000.0} s",
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        )

        var timerCurrentTime by remember {
            mutableStateOf(0L)
        }

        val timer by remember {
            mutableStateOf(object : CountDownTimer(
                timerCurrentTime,
                10
            ) {
                override fun onTick(millisUntilFinished : Long) {
                    restTime = millisUntilFinished
                }

                override fun onFinish() {
                    restTime = 0


                    val effect = VibrationEffect.createOneShot(
                        1000,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                }
            })
        }

        IconButton(
            onClick =
            {
                if (!isTimerOn) {
                    timerCurrentTime = (exercise.restTime.seconds)
                    timer.start()
                } else {
                    timerCurrentTime = timerCurrentTime
                    timer.cancel()
                }

                isTimerOn = !isTimerOn
            },
            modifier = Modifier.padding(end = 8.dp)
        )
        {
            Icon(
                imageVector = if (isTimerOn) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = "Start Timer",
                tint = MaterialTheme.colors.secondary,
            )
        }

        IconButton(
            enabled = isTimerOn,
            onClick =
            {
                timer.cancel()
                isTimerOn = !isTimerOn
            },
            modifier = Modifier.padding(end = 8.dp)
        )
        {
            Icon(
                imageVector = Icons.Filled.Stop,
                contentDescription = "Stop timer",
                tint = if (isTimerOn) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
            )
        }
    }
}
