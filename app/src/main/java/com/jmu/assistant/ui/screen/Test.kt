package com.jmu.assistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jmu.assistant.models.Course
import com.jmu.assistant.models.CourseTable

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun CourseTableView() {
    var weeks = listOf("第一周", "周一", "周二", "周三", "周四", "周五")
    var courseIndex = listOf("1", "2", "3", "4","午休", "5", "6", "7", "8", "9", "10")
    Row(Modifier.fillMaxSize()) {
        weeks.forEachIndexed { index, it ->
            Column(
                modifier = Modifier
                    .weight(if (it == "Index") 0.1f else 0.18f)
                    .fillMaxHeight()
            , verticalArrangement = SpaceBetween
            ) {
                TextButton(onClick = {}) {
                    Text(text = it)
                }
                if (index == 0) {
                    courseIndex.forEach {
                        Text(text = it)
                    }
                }

            }
        }
    }
}
