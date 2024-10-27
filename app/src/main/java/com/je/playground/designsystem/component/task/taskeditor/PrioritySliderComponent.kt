package com.je.playground.designsystem.component.task.taskeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.ui.theme.PlaygroundTheme

@Composable
fun PrioritySliderComponent(
    priority: Int,
    onPriorityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
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
        if ((0.0..0.5).contains(sliderPosition.toDouble())) com.je.playground.ui.theme.lowPriority
        else if ((0.5..1.5).contains(sliderPosition.toDouble())) com.je.playground.ui.theme.mediumPriority
        else com.je.playground.ui.theme.highPriority

    var isFocused by remember {
        mutableStateOf(false)
    }

    Surface {
        Column(
            modifier = Modifier.then(modifier)
        ) {
            Text(
                text = stringResource(R.string.priority),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 12.dp),
            )

            Text(
                text =
                if ((0.0..0.5).contains(sliderPosition.toDouble())) stringResource(R.string.low)
                else if ((0.5..1.5).contains(sliderPosition.toDouble())) stringResource(R.string.medium)
                else stringResource(R.string.high),
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
                    onPriorityChanged(sliderPosition.toInt())
                },
                valueRange = 0f..2f,
                steps = 1,
                colors = SliderDefaults.colors(
                    activeTrackColor = sliderColor,
                    inactiveTrackColor = sliderColor,
                ),
                modifier = Modifier
                    .height(30.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun PrioritySliderPreview0() {
    PlaygroundTheme {
        Surface {
            PrioritySliderComponent(
                priority = 0,
                onPriorityChanged = {},
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun PrioritySliderPreview1() {
    PlaygroundTheme {
        Surface {
            PrioritySliderComponent(
                priority = 1,
                onPriorityChanged = {},
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun PrioritySliderPreview2() {
    PlaygroundTheme {
        Surface {
            PrioritySliderComponent(
                priority = 2,
                onPriorityChanged = {},
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}