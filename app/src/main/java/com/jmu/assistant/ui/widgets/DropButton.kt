package com.jmu.assistant.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp


@Composable
fun DropDownButton(
    items: List<String>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(10.dp, 10.dp)
) {
    var index by remember {
        mutableStateOf(0)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    TextButton(
//        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.primary),
        onClick = { expanded = !expanded },
        modifier = modifier
    ) {
        Text(text = items[index], color = MaterialTheme.colorScheme.background)
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = offset
        ) {
            items.forEachIndexed { i, v ->
                DropdownMenuItem(onClick = { index = i;expanded = false;onClick(i) }) {
                    Text(text = v)
                }
            }
        }
    }

}