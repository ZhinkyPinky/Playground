package com.je.playground.ui.taskeditor.datetimepicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.R
import com.je.playground.ui.theme.regularText
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DateTimePicker(
    context : Context,
    title : String,
    savedDate : LocalDate?,
    savedTime : LocalTime?,
    onDateValueChange : (LocalDate?) -> Unit,
    onTimeValueChange : (LocalTime?) -> Unit
) {

    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colors.secondaryVariant
        )

        var newDate : LocalDate? by remember {
            mutableStateOf(null)
        }

        val clearDate = {
            newDate = null
            onDateValueChange(newDate)
        }

        var isDateExpanded by remember {
            mutableStateOf(false)
        }

        var newTime : LocalTime? by remember {
            mutableStateOf(null)
        }

        val clearTime = {
            newTime = null
            onTimeValueChange(newTime)
        }

        var isTimeExpanded by remember {
            mutableStateOf(false)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            DatePickerComponent(
                date = if (newDate != null) newDate.toString() else savedDate?.toString() ?: "",
                clickable = true,
                onClick = {
                    newDate = null
                    isTimeExpanded = false
                    isDateExpanded = !isDateExpanded
                },
                clearDate = clearDate,
                modifier = Modifier.weight(1f)
            )
            TimePickerComponent(
                time = if(newTime != null) newTime.toString() else savedTime?.toString() ?: "",
                context = context,
                clickable = true,
                onClick = {
                    newDate = null
                    isDateExpanded = false
                    isTimeExpanded = !isTimeExpanded
                },
                clearTime = clearTime,
                onValueChange = {
                    newTime = it
                    onTimeValueChange(it)},
                modifier = Modifier.weight(1f)
            )
        }
        Divider()

        if (isDateExpanded) {
            MonthAndYearComponent {
                newDate = it
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        newDate = null
                        isDateExpanded = false
                    },
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colors.secondary
                    )
                }
                TextButton(
                    onClick = {
                        onDateValueChange(newDate)
                        isDateExpanded = false
                    },
                ) {
                    Text(
                        text = "OK",
                        color = MaterialTheme.colors.secondary
                    )
                }
            }

            Divider()
        }
    }
}

@Composable
fun DatePickerComponent(
    date : String,
    clickable : Boolean,
    onClick : () -> Unit,
    clearDate : () -> Unit,
    modifier : Modifier
) {
    TextField(
        value = date,
        onValueChange = {},
        label = { Text(text = "Date") },
        trailingIcon = {
            if (date != "") {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear text-field",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { clearDate() }
                )
            }
        },
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
            .clickable(clickable) {
                onClick()
            }
            .then(modifier)
    )
}

@Composable
fun TimePickerComponent(
    time : String,
    context : Context,
    clickable : Boolean,
    onClick : () -> Unit,
    onValueChange : (LocalTime) -> Unit,
    clearTime : () -> Unit,
    modifier : Modifier
) {
    var textFieldValue by remember {
        mutableStateOf("")
    }

    TextField(
        value = textFieldValue,
        onValueChange = {},
        label = { Text(text = "Time") },
        trailingIcon = {
            if (time != "") {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear text-field",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { clearTime() }
                )
            }
        },
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
            .clickable(clickable) {
                timePicker(context = context) { _ : TimePicker, hour : Int, minute : Int ->
                    val date = LocalTime.of(
                        hour,
                        minute
                        )

                    textFieldValue = date.toString()
                    onValueChange(date)
                }
            }
            .then(modifier)
    )
}

private fun timePicker(
    context : Context,
    listener : TimePickerDialog.OnTimeSetListener
) {
    TimePickerDialog(
        context,
        R.style.Theme,
        listener,
        12,
        0,
        true
    ).show()
}

private fun datePicker(
    context : Context,
    listener : DatePickerDialog.OnDateSetListener
) {
    val year : Int = LocalDate.now().year
    val month : Int = LocalDate.now().monthValue - 1
    val day : Int = LocalDate.now().dayOfMonth

    DatePickerDialog(
        context,
        listener,
        year,
        month,
        day
    ).show()
}