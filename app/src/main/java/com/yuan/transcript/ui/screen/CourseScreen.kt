package com.yuan.transcript.ui.screen


import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.yuan.transcript.ui.widgets.DropDownButton
import com.yuan.transcript.ui.widgets.ProgressDialog
import com.yuan.transcript.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(mainNavController: NavHostController, cookie: String) {
    val viewModel: CourseViewModel = viewModel()
    viewModel.cookie = cookie
    val menuItem = listOf("2021-2022 第二学期", "2021-2022 第一学期")
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(text = "Course") },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            )
        )
    }) {
        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(70.dp))
            if (!viewModel.loadCourse) SelectionContainer {

                Text(
                    text = viewModel.ics, modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    )
                )
            }
        }
        DropDownButton(
            items = menuItem, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), offset = DpOffset(20.dp, 120.dp), onClick = {
                viewModel.semesterIndex = it
                viewModel.getCourseTable()
                viewModel.loadCourse = true
            })
        if (viewModel.loadCourse) ProgressDialog("正在加载中请稍后……")
    }
}