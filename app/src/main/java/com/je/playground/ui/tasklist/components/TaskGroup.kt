package com.je.playground.ui.tasklist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskGroup(testGroup : List<Int>) {
    Box(
        modifier = Modifier
            .background(
                color = Color.Cyan
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                start = 6.dp,
                end = 6.dp,
                top = 6.dp,
                bottom = 6.dp
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {

        /*testGroup.forEach {
            MainTaskComponent(
                taskWithOccasions = TaskWithOccasions(
                    task = Task(id = it.toLong()),
                    simpleTask = SimpleTask(
                        id = it.toLong(),
                        name = "test {it}",
                        priority = 1,
                        note = "The brown dog jumped of the lazy fox."
                    ),
                    exerciseProgramWithExercises = null,
                    weekdaySchedule = emptyList(),
                    taskOccasions = emptyList()
                ),
                updateTaskOccasion = {},
                deleteTask = {}
            )
        }*/

    }
}
