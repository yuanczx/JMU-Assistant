package com.jmu.assistant.ui.screen


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.R
import com.jmu.assistant.ui.widgets.*
import com.jmu.assistant.viewmodel.CourseViewModel
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@SuppressLint("SdCardPath")
@Composable
fun CourseScreen(navController: NavHostController) {
    /**
     * @Author yuanczx
     * @Description 课表界面
     * @Date 2022/3/10 19:26
     * @Params []
     * @Return
     **/

    val scope = rememberCoroutineScope()
    val viewModel: CourseViewModel = viewModel()

    val boxWidth = LocalConfiguration.current.screenWidthDp / 6
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val wideLabel = screenHeight < 720
    LaunchedEffect(key1 = null, block = {
        if (viewModel.loadFinish) return@LaunchedEffect
        viewModel.getCourseTable()
        viewModel.makeCourseTable()
        viewModel.loadFinish = true
    })

    Log.d("Screen", "CourseScreen: $screenHeight")

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TopBar(titleId = R.string.Course, navigationIcon = { BackIcon(navController) }) {
            TextButtonDropMenu(
                title = viewModel.semesterItem[viewModel.semesterIndex],
                list = viewModel.semesterItem
            ) {
                if (viewModel.semesterIndex != it) scope.launch {
                    viewModel.semesterIndex = it //设置索引
                    viewModel.getCourseTable() //获取课程表
                    viewModel.makeCourseTable(1)
                    viewModel.loadFinish = true
                }
            }

            TextButtonDropMenu(
                title = "第${viewModel.weekSelector}周",
                repeatTimes = 18,
                item = { "第${it + 1}周" })
            {
                viewModel.makeCourseTable(it + 1)
            }

            IconDropMenu(
                painter = rememberVectorPainter(image = Icons.Default.MoreVert),
                contentDescriptor = "More",
                list = listOf(
                    Pair(stringResource(id = R.string.export_ics)) {},
                    Pair(stringResource(id = R.string.show_weekend)) {
                        Checkbox(
                            checked = viewModel.showWeekend,
                            onCheckedChange = {
                                viewModel.showWeekend = it
                                viewModel.handle = true
                            }
                        )
                    },
                    Pair(stringResource(id = R.string.refresh)) {},
                ),
                onClick = {
                    when (it) {
                        0 -> {
//                          导出ICS文件
                            viewModel.buildICS()
                            viewModel.exportICS()
                        }
                        1 -> {
                            //显示周末
                            viewModel.showWeekend = !viewModel.showWeekend
                        }
                        2 -> {
                            //刷新页面
                            scope.launch {
                                viewModel.getCourseTable()
                            }
                        }
                    }
                })
        }

        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(0.08f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                //时间轴
                repeat(5) {

                    val firstSection = if (!wideLabel) viewModel.courseTime[it * 2]
                    else viewModel.courseTime[it * 2].replace("\n", "-")
                    val secondSection = if (!wideLabel) viewModel.courseTime[it * 2 + 1]
                    else viewModel.courseTime[it * 2 + 1].replace("\n", "-")
                    if (it == 0) Text(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        text = stringResource(R.string.time),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.padding(1.dp)
                    )
                    Column(
                        modifier = Modifier.weight(0.19f),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (it * 2 + 1).toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(text = firstSection, fontSize = 12.sp)
                        Divider(Modifier.height(1.dp))
                        Text(
                            text = (it * 2 + 2).toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(text = secondSection, fontSize = 12.sp)
                        if (it != 1 && it != 3 && it != 4) Divider()
                    }
                    Divider(
                        thickness = if (it == 1 || it == 3 || it == 4) 15.dp else 1.dp,
                        color = Color.Transparent
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(0.92f)
                    .horizontalScroll(rememberScrollState())
            ) {
                repeat(if (viewModel.showWeekend) 7 else 5) { weekday ->
                    Column(
                        modifier = if (viewModel.showWeekend) Modifier
                            .fillMaxHeight()
                            .requiredWidth(boxWidth.dp)
                        else Modifier
                            .weight(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        repeat(5) {
                            if (it == 0) {
                                Text(
                                    fontWeight = FontWeight.Light,
                                    text = viewModel.weekDayName[weekday],
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.Serif,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                            if (viewModel.loadFinish) Box(
                                modifier = Modifier
                                    .weight(0.19f)
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .combinedClickable(
                                        onLongClickLabel = "add_event",
                                        onLongClick = {
                                            viewModel.semesterCourse[weekday + 1].let { dayCourse ->
                                                if (dayCourse.isNullOrEmpty()) {
                                                    viewModel.semesterCourse[weekday + 1] =
                                                        mutableStateMapOf()
                                                }
                                                viewModel.addCourse = true
                                                viewModel.currentSelect = Pair(weekday, it)
                                            }
                                        }) {}
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer.copy(
                                            alpha = 0.35f
                                        )
                                    )
                            ) {
                                viewModel.semesterCourse[viewModel.weekSelector]?.get(weekday + 1)
                                    ?.get(it * 2 + 1)?.let {
                                    Column(
                                        Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                viewModel.toast(
                                                    "${it.first} \n ${
                                                        it.second.replace(
                                                            Regex("\\s"),
                                                            " "
                                                        )
                                                    }"
                                                )
                                            },
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = it.first,
                                            maxLines = 3,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .padding(
                                                    top = 5.dp,
                                                    start = 2.dp,
                                                    end = 2.dp
                                                )
                                        )
                                        Text(
                                            text = it.second,
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp,
                                            maxLines = 2,
                                            modifier = Modifier.padding(2.dp)
                                        )
                                        Text(
                                            text = it.third,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Light,
                                            modifier = Modifier.padding(
                                                bottom = 5.dp,
                                                start = 2.dp,
                                                end = 2.dp
                                            )
                                        )
                                    }
                                }
                            }
                            if (it == 1 || it == 3 || it == 4) Divider(
                                thickness = 15.dp,
                                color = Color.Transparent
                            )
                        }
                    }
                }
            }
        }


    }

    if (viewModel.loadCourse) ProgressDialog(stringResource(id = R.string.wait_loading))
    if (viewModel.addCourse) {
        var courseName by remember { mutableStateOf("") }
        var room by remember { mutableStateOf("") }
        var teacher by remember { mutableStateOf("") }
        AlertDialog(title = {
            Text(text = stringResource(id = R.string.add_course))
        }, text = {
            Column {
                TextField(value = courseName, onValueChange = { courseName = it })
                TextField(value = room, onValueChange = { room = it })
                TextField(value = teacher, onValueChange = { teacher = it })
            }
        }, onDismissRequest = { viewModel.addCourse = false }, confirmButton = {
            Button(onClick = {
                viewModel.addCourse = false
                viewModel.semesterCourse[viewModel.weekSelector]!![viewModel.currentSelect.first + 1]!![viewModel.currentSelect.second * 2 + 1] =
                    Triple(courseName, room, teacher)
            }) {
                Text(text = stringResource(R.string.add))
            }
        }, dismissButton = {
            Button(onClick = { viewModel.addCourse = false }) {
                Text(text = stringResource(R.string.dismiss))
            }
        })
    }
}



