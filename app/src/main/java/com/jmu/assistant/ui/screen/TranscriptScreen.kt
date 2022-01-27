package com.jmu.assistant.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.ui.widgets.GradeList
import com.jmu.assistant.viewmodel.TranscriptViewModel
import org.jsoup.nodes.Element
import com.jmu.assistant.ui.widgets.AlertDialog
import kotlinx.coroutines.launch

@Composable
fun MainActivity.TranscriptScreen() {
    val viewModel: TranscriptViewModel = viewModel()
    val scope = rememberCoroutineScope()
    mainViewModel.title = stringResource(id = R.string.Transcript)//设置标题
    //Tab点击事件
    fun clickTab(index: Int, element: Element) {
        if (viewModel.selectedTab == index) return
        viewModel.selectedTab = index
        viewModel.semesterId = element.`val`() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //显示学期Tabs
        if (viewModel.showTabs) TabRow(
            selectedTabIndex = viewModel.selectedTab,
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background
        ) {
            viewModel.semesters?.let {
                it.forEachIndexed { index, element ->
                    Tab(
                        selected = index == viewModel.selectedTab,
                        onClick = { clickTab(index, element) },
                        text = {
                            Text(
                                text = element.html(),
                                color = MaterialTheme.colorScheme.background,
                                textAlign = TextAlign.Center
                            )
                        })
                }
            }
        }
        // 加载中显示
        AnimatedVisibility(visible = viewModel.loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 120.dp)
            )
        }

        //显示成绩列表
        if (!viewModel.loading) viewModel.semesterSelector()?.let {
            GradeList(courseInfos = it)
        }

        //加载错误显示提示框
        if (viewModel.error) AlertDialog(title = "请求错误", text = "加载失败是否重新尝试？",
            onConfirm = { viewModel.error = false;scope.launch { viewModel.getSemesterIndex();viewModel.getGrade() } },
            onDismiss = { viewModel.error = false })
    }

    //第一次启动加载
    LaunchedEffect(null) {
        viewModel.loading = true
        viewModel.getSemesterIndex()
        viewModel.getGrade()
        viewModel.loading = false
    }
}