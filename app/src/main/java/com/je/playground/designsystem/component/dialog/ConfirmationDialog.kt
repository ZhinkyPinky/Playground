package com.je.playground.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    title : String,
    contentText : String,
    confirmButtonText : String,
    confirm : () -> Unit,
    onDismissRequest : () -> Unit,
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .wrapContentHeight()
                .wrapContentWidth()
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 12.dp
                        )
                )

                Divider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 6.dp
                    )
                )

                Text(
                    text = contentText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp
                        )
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    ) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            confirm()
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    ) {
                        Text(text = confirmButtonText)
                    }
                }
            }
        }
    }
}