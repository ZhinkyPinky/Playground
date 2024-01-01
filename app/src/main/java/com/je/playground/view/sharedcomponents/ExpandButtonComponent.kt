package com.je.playground.view.sharedcomponents


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.je.playground.R

@Composable
fun ExpandButtonComponent(
    isExpanded : Boolean,
    modifier : Modifier = Modifier,
    onClick : () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.then(modifier)
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}