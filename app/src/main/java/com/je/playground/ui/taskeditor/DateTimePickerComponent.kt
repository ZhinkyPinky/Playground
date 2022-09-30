package com.je.playground.ui.taskeditor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.service.autofill.OnClickAction
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.R
import com.je.playground.ui.theme.regularText
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/*
@Composable fun dateTimePickerComponent(
    context : Context,
    title : String
) : String {
    var date : LocalDate? = null
    var time : LocalTime? = null

    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colors.secondaryVariant
        )

        Row {
            date = datePickerComponent(
                context = context,
            )

            time = timePickerComponent(
                context = context
            )
        }
    }

    return LocalDateTime
        .of(
            date,
            time
        )
        .toString()
}
 */

