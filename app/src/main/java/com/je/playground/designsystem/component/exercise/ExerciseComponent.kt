package com.je.playground.designsystem.component.exercise

import android.os.CountDownTimer
import android.os.VibrationEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.database.exerciseprogram.entity.Exercise

@Composable fun ExerciseComponent(exercise : Exercise) {
    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        SetsComponent(exercise = exercise)
        RepsComponent(exercise = exercise)
        RestComponent(exercise = exercise)
    }
}

@Composable
fun SetsComponent(exercise : Exercise) {
    Text(
        text = "Sets:  ${exercise.sets}",
        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            mutableLongStateOf(exercise.restTime.seconds)
        }

        Text(
            text = "Rest: ${restTime / 1000.0} s",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            mutableLongStateOf(0L)
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
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
                tint = if (isTimerOn) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
