package com.je.playground.deprecated

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText
import com.je.playground.ui.theme.subcontent
import java.time.LocalTime

@Composable
fun TimePickerComponent(context : Context) {
    var timeFrom by remember {
        mutableStateOf("")
    }

    var timeTo by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "Time")
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = timeFrom,
                onValueChange = { timeFrom = it },
                label = { Text(text = "From") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colors.secondary),
                colors = TextFieldDefaults.textFieldColors(
                    disabledLabelColor = MaterialTheme.colors.secondaryVariant,
                    focusedLabelColor = MaterialTheme.colors.secondary,
                    cursorColor = MaterialTheme.colors.secondaryVariant,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.background
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        timePicker(context) { _ : TimePicker, hour : Int, minute : Int ->
                            timeFrom = LocalTime
                                .of(
                                    hour,
                                    minute
                                )
                                .toString()
                        }
                    }
            )

            TextField(
                value = timeTo,
                onValueChange = { timeTo = it },
                label = { Text(text = "To") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colors.secondary),
                colors = TextFieldDefaults.textFieldColors(
                    disabledLabelColor = MaterialTheme.colors.secondaryVariant,
                    focusedLabelColor = MaterialTheme.colors.secondary,
                    cursorColor = MaterialTheme.colors.secondaryVariant,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.background
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .clickable(timeFrom != "") {
                        timePicker(context) { _ : TimePicker, hour : Int, minute : Int ->
                            timeTo = LocalTime
                                .of(
                                    hour,
                                    minute
                                )
                                .toString()
                        }
                    }
            )
        }
    }
}

private fun timePicker(
    context : Context,
    listener : TimePickerDialog.OnTimeSetListener
) {
    TimePickerDialog(
        context,
        listener,
        12,
        0,
        true
    ).show()
}


@Composable
fun OuterCircleTextButtons(onClick: (LocalTime) -> Unit) {
    val angleBetweenButtons = 360f / 12

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        (12..23).forEachIndexed { index, hour ->
            TextButton(
                onClick = { onClick(LocalTime.of(hour, 0)) },
                modifier = Modifier
                    .rotate(angleBetweenButtons * index.toFloat())
                    .size(35.dp)
                    .padding(64.dp)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colors.background)
            ) {
                Text(text = "$hour",
                     textAlign = TextAlign.Center,
                     style = subcontent(MaterialTheme.colors.secondary)
                )
            }
        }
    }
}

@Composable
fun InnerCircleTextButtons(onClick: (LocalTime) -> Unit) {
    val angleBetweenButtons = 360f / 12

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        (0..11).forEachIndexed { index, hour ->
            TextButton(
                onClick = { onClick(LocalTime.of(hour, 0)) },
                modifier = Modifier.rotate(angleBetweenButtons * index.toFloat())
                    .size(35.dp)
                    .padding(8.dp)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colors.background)
            ) {
                Text(text = "$hour",
                     textAlign = TextAlign.Center,
                     style = subcontent(MaterialTheme.colors.secondary)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCircleTextButtons() {
    OuterCircleTextButtons(onClick = { localTime ->
        // handle LocalTime here
    })
    InnerCircleTextButtons(onClick = { localTime ->
        // handle LocalTime here
    })
}




