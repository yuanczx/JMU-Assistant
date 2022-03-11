package com.jmu.assistant.ui.screen


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CourseScreen(navController: NavHostController) {
    /**
     * @Author yuanczx
     * @Description 课表界面
     * @Date 2022/3/10 19:26
     * @Params []
     * @Return
     **/
    val viewModel: CourseViewModel = viewModel()

    LaunchedEffect(key1 = null, block = {
        viewModel.getCourseTable()
        viewModel.makeCourseTable()
        viewModel.loadFinish = true
    })

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TopBar(stringId = R.string.Course, navigationIcon = { BackIcon(navController) }) {
            TextButtonDropMenu(
                title = viewModel.semesterItem[viewModel.semesterIndex],
                list = viewModel.semesterItem
            ) {
                if (viewModel.semesterIndex != it) scope.launch {
                    viewModel.semesterIndex = it //设置索引
                    viewModel.weekSelector = 1
                    viewModel.getCourseTable() //获取课程表
                    viewModel.makeCourseTable()
                    viewModel.loadFinish = true
                }
            }

            TextButtonDropMenu(
                title = "第${viewModel.weekSelector}周",
                repeatTimes = 18,
                item = { "第${it + 1}周" })
            {
                viewModel.loadFinish = false
                viewModel.weekSelector = it + 1
                viewModel.makeCourseTable()
                viewModel.loadFinish = true
            }


            IconDropMenu(
                painter = rememberVectorPainter(image = Icons.Default.MoreVert),
                contentDescriptor = "More",
                list = listOf(
                    stringResource(id = R.string.export_ics),
                    stringResource(id = R.string.refresh)
                ),
                onClick = {
                    when (it) {
                        0 -> {
                            viewModel.buildICS()
                            viewModel.exportICS()
                        }
                        1 -> {
                            scope.launch {
                                viewModel.getCourseTable()
                            }
                        }
                    }
                })
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(6) {
                if (it == 0) Text(
                    modifier = Modifier.weight(0.08f),
                    text = stringResource(R.string.time),
                    textAlign = TextAlign.Center
                )
                else Text(
                    modifier = Modifier.weight(0.184f),
                    text = viewModel.weekDayName[it - 1],
                    textAlign = TextAlign.Center
                )
            }
        }

        if (viewModel.loadFinish)
            Row(
                Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
            ) {
                repeat(6) { weekday ->
                    Column(
                        modifier = Modifier
                            .weight(if (weekday == 0) 0.08f else 0.184f)
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
                                //分界线
                                Divider(
                                    Modifier.height(1.dp),
                                    color = if (it != 1 && it != 3 && it != 4) MaterialTheme.colorScheme.onSurface.copy(
                                        0.12f
                                    )
                                    else Color.Transparent
                                )
                            }
                            if (it == 3 || it == 1) Divider(
                                Modifier.weight(0.025f),
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                        else repeat(5) {
                            Box(
                                modifier = Modifier
                                    .weight(0.19f)
                                    .fillMaxSize()
                                    .padding(1.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(
                                        MaterialTheme.colorScheme.secondaryContainer.copy(
                                            alpha = 0.35f
                                        )
                                    )
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
                            if (it == 3 || it == 1) Divider(
                                Modifier.weight(0.025f),
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                        Divider(Modifier.height(10.dp), color = MaterialTheme.colorScheme.surface)
                    }
                }
            }

    }

    if (viewModel.loadCourse) ProgressDialog(stringResource(id = R.string.wait_loading))
}