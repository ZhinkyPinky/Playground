package com.je.playground.ui.taskeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrioritySliderComponent(
    onValueChangeFinished : (String) -> Unit
) {
    var sliderPosition by remember {
        mutableStateOf(0f)
    }

    val sliderColor =
        if ((0.0..0.5).contains(sliderPosition.toDouble())) MaterialTheme.colors.onPrimary
        else if ((0.5..1.5).contains(sliderPosition.toDouble())) Color(0xFFFFAB00)
        else Color.Red

    val priority = if ((0.0..0.5).contains(sliderPosition.toDouble())) "Low"
    else if ((0.5..1.5).contains(sliderPosition.toDouble())) "Medium"
    else "High"

    var isFocused by remember {
        mutableStateOf(false)
    }

    Column {
        Text(
            text = "Priority",
            fontSize = 12.sp,
            color = if (isFocused) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
            )
        )

        Text(
            text = priority,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 4.dp
            )
        )

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                isFocused = true
            },
            onValueChangeFinished = {
                isFocused = false
                onValueChangeFinished(priority)
            },
            valueRange = 0f..2f,
            steps = 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.secondary,
                activeTrackColor = sliderColor,
                inactiveTrackColor = sliderColor,
                activeTickColor = Color.Transparent
            ),
            modifier = Modifier
                .height(30.dp)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                )
        )

        Divider(color = MaterialTheme.colors.primaryVariant)
    }
}