package com.je.playground.designsystem.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(
                    modifier = Modifier.height(16.dp),
                )

                Text(text = text)

                Spacer(
                    modifier = Modifier.height(24.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            onConfirm()
                            onDismissRequest()
                        },
                    ) {
                        Text(text = confirmButtonText)
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun ConfirmationDialogPreview() {
    PlaygroundTheme {
        ConfirmationDialog(
            title = stringResource(R.string.delete),
            text = stringResource(R.string.delete_task_warning),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {},
            onDismissRequest = {}
        )
    }
}