package com.jmu.assistant.ui.screen


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.ui.widgets.DropDownButton
import com.jmu.assistant.ui.widgets.ProgressDialog
import com.jmu.assistant.viewmodel.CourseViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen(mainNavController: NavHostController) {
    val viewModel: CourseViewModel = viewModel()
    val menuItem = listOf("请选择学期","2021-2022 第二学期", "2021-2022 第一学期")
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize()) {
        DropDownButton(
            items = menuItem, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), offset = DpOffset(20.dp,(-220).dp), onClick = {
                viewModel.semesterIndex = it-1
                if (it!=0)scope.launch {
                    viewModel.getCourseTable()
                }
            })
        if (!viewModel.loadCourse) SelectionContainer {
            Text(
                text = viewModel.ics, modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )
            )
        }

        if (viewModel.loadCourse) ProgressDialog("正在加载中请稍后……")
    }
}