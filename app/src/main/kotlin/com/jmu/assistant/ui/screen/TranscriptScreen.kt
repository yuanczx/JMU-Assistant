package com.jmu.assistant.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.R
import com.jmu.assistant.ui.widgets.AlertDialog
import com.jmu.assistant.ui.widgets.BackIcon
import com.jmu.assistant.ui.widgets.GradeItem
import com.jmu.assistant.ui.widgets.TopBar
import com.jmu.assistant.viewmodel.TranscriptViewModel
import kotlinx.coroutines.launch
import org.jsoup.nodes.Element

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun TranscriptScreen(navHostController: NavHostController) {
    /**
     * @Author yuanczx
     * @Description 成绩单界面
     * @Date 2022/3/10 19:25
     * @Params []
     * @Return
     **/
    val viewModel: TranscriptViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }

    fun clickTab(index: Int, element: Element) {
        /**
         * @Author yuanczx
         * @Description 学期Tab点击事件
         * @Date 2022/3/10 19:25
         * @Params [index, element]
         * @Return
         **/
        if (viewModel.selectedTab == index) return
        viewModel.selectedTab = index
        viewModel.semesterId = element.`val`()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(R.string.Transcript, scrollBehavior, navigationIcon = {
                BackIcon(
                    navController = navHostController
                )
            })
        }
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            //显示学期Tabs
            if (viewModel.showTabs) ScrollableTabRow(
                selectedTabIndex = viewModel.selectedTab,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                edgePadding = 5.dp,
                indicator = @Composable { tabPositions ->
                    TabRowDefaults.Indicator(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[viewModel.selectedTab])
                    )
                }
            ) {
                viewModel.semesters?.let {
                    it.forEachIndexed { index, element ->
                        Tab(selected = index == viewModel.selectedTab,
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
            if (!viewModel.loading) viewModel.semesterSelector()?.let { courses ->
                LazyVerticalGrid(GridCells.Fixed(2), content = {
                    items(courses.size) {
                        GradeItem(
                            title = courses[it].course.nameZh,
                            subTitle = courses[it].gaGrade,
                            gpa = courses[it].gp.toString(),
                            info = courses[it].course.credits.toString()
                        )
                    }
                })
            }

            //加载错误显示提示框
            if (viewModel.error) AlertDialog(title = "请求错误", text = "加载失败是否重新尝试？",
                onConfirm = {
                    viewModel.error =
                        false;scope.launch { viewModel.getSemesterIndex();viewModel.getGrade() }
                },
                onDismiss = { viewModel.error = false })
        }

    }
    //第一次启动加载
    LaunchedEffect(null) {
        if (viewModel.transcript != null) return@LaunchedEffect
        viewModel.loading = true
        viewModel.getSemesterIndex()
        viewModel.getGrade()
        viewModel.loading = false
    }
}