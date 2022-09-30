package com.je.playground.ui.taskeditor

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText
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