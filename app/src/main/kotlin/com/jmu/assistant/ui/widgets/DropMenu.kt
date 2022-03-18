package com.jmu.assistant.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun TextButtonDropMenu(
    title: String,
    list: List<String>,
    titleColor: Color = MaterialTheme.colorScheme.onPrimary,
    itemTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: (Int) -> Unit,
) {
    /**
     * @Author yuanczx
     * @Description 文字样式下拉菜单
     * @Date 2022/3/10 19:30
     * @Params [title, list, titleColor, itemTextColor, onClick]
     * @Return
     **/
    var expanded by remember { mutableStateOf(false) }
    TextButton(onClick = { expanded = !expanded }) {
        Text(text = title, color = titleColor)
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            list.forEachIndexed { index, value ->
                DropdownMenuItem(text = { Text(text = value, color = itemTextColor) }, onClick = {
                    expanded = false
                    onClick(index)
                })
            }
        }
    }
}


@Composable
fun TextButtonDropMenu(
    title: String,
    repeatTimes: Int,
    titleColor: Color = MaterialTheme.colorScheme.onPrimary,
    itemTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    item: (Int) -> String,
    onClick: (Int) -> Unit,
) {
    /**
     * @Author yuanczx
     * @Description 重复文字下拉菜单
     * @Date 2022/3/10 19:31
     * @Params [title, repeatTimes, titleColor, itemTextColor, item, onClick]
     * @Return
     **/
    var expanded by remember { mutableStateOf(false) }
    TextButton(onClick = { expanded = !expanded }) {
        Text(text = title, color = titleColor)
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            repeat(repeatTimes) {
                DropdownMenuItem(
                    text = { Text(text = item(it), color = itemTextColor) },
                    onClick = {
                        expanded = false
                        onClick(it)
                    })
            }
        }
    }
}

@Composable
fun IconDropMenu(
    painter: Painter,
    contentDescriptor: String,
    list: List<Pair<String, @Composable () -> Unit>>,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    itemTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: (Int) -> Unit,
) {
    /**
     * @Author yuanczx
     * @Description 图标样式下拉菜单
     * @Date 2022/3/10 19:31
     * @Params [painter, contentDescriptor, list, tint, itemTextColor, onClick]
     * @Return
     **/
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(painter = painter, contentDescription = contentDescriptor, tint = tint)
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            list.forEachIndexed { index, value ->
                DropdownMenuItem(
                    text = { Text(text = value.first) },
                    trailingIcon = value.second,
                    onClick = {
                        expanded = false
                        onClick(index)
                    })
            }
        }
    }
}

@JvmName("IconDropMenu1")
@Composable
fun IconDropMenu(
    painter: Painter,
    contentDescriptor: String,
    list: List<String>,
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    itemTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: (Int) -> Unit,
) {
    val mlist = mutableListOf<Pair<String, @Composable () -> Unit>>()
    list.forEach {
        mlist+= listOf(Pair(it) {})
    }
    IconDropMenu(
        painter = painter,
        contentDescriptor = contentDescriptor,
        list = mlist,
        tint = tint,
        itemTextColor = itemTextColor,
        onClick = onClick,
    )
}