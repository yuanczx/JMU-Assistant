package com.jmu.assistant.ui.screen


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.ui.widgets.DropDownButton
import com.jmu.assistant.ui.widgets.ProgressDialog
import com.jmu.assistant.viewmodel.CourseViewModel
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@SuppressLint("SdCardPath")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainActivity.CourseScreen() {
    val viewModel: CourseViewModel = viewModel()

    LaunchedEffect(key1 = null, block = {
        mainViewModel.showTopBar = false
        viewModel.getCourseTable()
        viewModel.makeCourseTable()
        viewModel.loadFinish = true
    })
    val menuItem = listOf(
        stringResource(R.string.second_semester_21_22),
        stringResource(R.string.first_semester_21_22)
    )
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SmallTopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.background,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
            title = { Text(text = stringResource(id = R.string.Course)) },
            actions = {
                DropDownButton(
                    items = menuItem, modifier = Modifier
                        .padding(0.dp), onClick = {
                        if (viewModel.semesterIndex != it) scope.launch {
                            viewModel.semesterIndex = it //设置索引
                            viewModel.weekSelector = 1
                            viewModel.getCourseTable() //获取课程表
                            viewModel.makeCourseTable()
                            viewModel.loadFinish = true
                        }
                    })
                TextButton(onClick = { viewModel.showWeekSelector = true }) {
                    Text(
                        text = "第${viewModel.weekSelector}周",
                        color = MaterialTheme.colorScheme.background
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .height(250.dp)
                            .background(MaterialTheme.colorScheme.background),
                        expanded = viewModel.showWeekSelector,
                        onDismissRequest = { viewModel.showWeekSelector = false }) {
                        repeat(18) {
                            DropdownMenuItem(onClick = {
                                viewModel.loadFinish = false
                                viewModel.weekSelector =
                                    it + 1;viewModel.makeCourseTable();viewModel.showWeekSelector =
                                false;viewModel.loadFinish = true
                            }) {
                                Text(text = "第${it + 1}周")
                            }
                        }
                    }
                }

                IconButton(onClick = { viewModel.actionMore = !viewModel.actionMore }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.background
                    )
                    DropdownMenu(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        expanded = viewModel.actionMore,
                        onDismissRequest = { viewModel.actionMore = false }) {
                        DropdownMenuItem(onClick = {
                            viewModel.buildICS()
                            viewModel.exportICS()
                        }) {
                            Text(text = stringResource(R.string.export_ics))
                        }
                        DropdownMenuItem(onClick = {
                            scope.launch {
                                viewModel.getCourseTable()
                            }
                        }) {
                            Text(text = stringResource(R.string.refresh))
                        }
                    }
                }
            })


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(6) {
                if (it == 0) Text(
                    modifier = Modifier.weight(0.1f),
                    text = stringResource(R.string.time),
                    textAlign = TextAlign.Center
                )
                else Text(
                    modifier = Modifier.weight(0.18f),
                    text = "周 $it",
                    textAlign = TextAlign.Center
                )
            }
        }

        if (viewModel.loadFinish)
            Row(Modifier.fillMaxSize()) {
                repeat(6) { weekday ->
                    Column(
                        modifier = Modifier
                            .weight(if (weekday == 0) 0.1f else 0.18f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (weekday == 0) repeat(5) {
                            Column(
                                modifier = Modifier
                                    .weight(0.19f)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = (it * 2 + 1).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(text = viewModel.courseTime[it * 2], fontSize = 12.sp)
                                Divider(Modifier.height(1.dp))
                                Text(
                                    text = (it * 2 + 2).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(text = viewModel.courseTime[it * 2 + 1], fontSize = 12.sp)
                                Divider(
                                    Modifier.height(1.dp),
                                    color = if (it != 1 && it != 3 && it != 4) MaterialTheme.colorScheme.onSurface.copy(
                                        0.12f
                                    )
                                    else Color.Transparent
                                )
                            }
                            if (it == 3 || it == 1) Divider(Modifier.weight(0.025f))
                        }
                        else repeat(5) {
                            Box(
                                modifier = Modifier
                                    .weight(0.19f)
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                viewModel.weekCourse[weekday]?.get(it * 2 + 1)?.let {
                                    Column(
                                        Modifier
                                            .fillMaxSize()
                                            .clickable { viewModel.toast(it.first + it.second) },
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
                            if (it == 3 || it == 1) Divider(Modifier.weight(0.025f))
                        }
                        Divider(Modifier.height(10.dp))
                    }
                }
            }

    }

    mainViewModel.title = stringResource(id = R.string.Course)
    if (viewModel.loadCourse) ProgressDialog(stringResource(id = R.string.wait_loading))
}