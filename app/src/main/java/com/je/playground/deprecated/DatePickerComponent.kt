package com.je.playground.deprecated

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText
import java.time.LocalDate

@Composable
fun DatePickerComponent(context : Context) {
    var dateFrom by remember {
        mutableStateOf("")
    }

    var dateTo by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "Date")
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = dateFrom,
                onValueChange = {},
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
                        datePicker(context) { _ : DatePicker, year : Int, month : Int, day : Int ->
                            dateFrom = LocalDate
                                .of(
                                    year,
                                    month,
                                    day
                                )
                                .toString()
                        }
                    }
            )

            TextField(
                value = dateTo,
                onValueChange = {},
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
                    .clickable(dateFrom != "") {
                        datePicker(context) { _ : DatePicker, year : Int, month : Int, day : Int ->
                            dateTo = LocalDate
                                .of(
                                    year,
                                    month,
                                    day
                                )
                                .toString()
                        }
                    }
            )
        }
    }
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

