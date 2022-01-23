package com.yuan.transcript.ui.widgets

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlin.math.exp


@Composable
fun DropDownButton(items: List<String>,onClick:(Int)->Unit,modifier: Modifier=Modifier,offset:DpOffset= DpOffset(10.dp,10.dp)) {
    var index by remember {
        mutableStateOf(0)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    Button(onClick = { expanded = !expanded },modifier=modifier) {
        Text(text = items[index])
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, offset = offset) {
        items.forEachIndexed { i, v ->
            DropdownMenuItem(onClick = { index = i ;expanded=false;onClick(i) }) {
                Text(text = v)
            }
        }
    }
}