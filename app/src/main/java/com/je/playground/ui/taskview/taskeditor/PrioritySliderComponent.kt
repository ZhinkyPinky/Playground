package com.je.playground.ui.taskview.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.ui.taskview.viewmodel.Priority

@Composable
fun PrioritySliderComponent(
    priority : Int,
    onPriorityChanged : (Int) -> Unit,
    modifier : Modifier = Modifier,
) {
    var sliderPosition by remember {
        mutableFloatStateOf(
            when (priority) {
                0 -> 0f
                1 -> 1f
                2 -> 2f
                else -> 0f
            }
        )
    }

    val sliderColor =
        if ((0.0..0.5).contains(sliderPosition.toDouble())) Color(0xFF00C853)
        else if ((0.5..1.5).contains(sliderPosition.toDouble())) Color(0xFFFFAB00)
        else Color.Red

    val priority =
        if ((0.0..0.5).contains(sliderPosition.toDouble())) "Low"
        else if ((0.5..1.5).contains(sliderPosition.toDouble())) "Medium"
        else "High"

    var isFocused by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(
                width = 1.dp,
                color = if (isFocused) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.background,
                shape = RectangleShape
            )
            .padding(
                6.dp
            )
            .then(modifier)
    ) {
        Text(
            text = "Priority",
            fontSize = 12.sp,
            color = if (isFocused) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(
                start = 12.dp,
            )
        )

        Text(
            text = priority,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(start = 12.dp)
        )

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                isFocused = true
            },
            onValueChangeFinished = {
                isFocused = false
                onPriorityChanged(Priority.valueOf(priority).ordinal)
            },
            valueRange = 0f..2f,
            steps = 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                activeTrackColor = sliderColor,
                inactiveTrackColor = sliderColor,
                activeTickColor = Color.Transparent
            ),
            modifier = Modifier
                .height(30.dp)
        )
    }
}