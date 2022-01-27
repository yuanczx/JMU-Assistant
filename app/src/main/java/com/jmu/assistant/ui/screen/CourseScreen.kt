package com.jmu.assistant.ui.screen


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.*
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.ui.widgets.DropDownButton
import com.jmu.assistant.ui.widgets.ProgressDialog
import com.jmu.assistant.viewmodel.CourseViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

@SuppressLint("SdCardPath")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainActivity.CourseScreen(navController: NavHostController) {
    val viewModel: CourseViewModel = viewModel()

    val menuItem = listOf("请选择学期", "2021-2022 第二学期", "2021-2022 第一学期")
    val permission = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    )
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        DropDownButton(
            items = menuItem, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), offset = DpOffset(20.dp, (-220).dp), onClick = {
                if (it != 0) scope.launch {
                    viewModel.semesterIndex = it - 1 //设置索引
                    viewModel.getCourseTable() //获取课程表
                    mainViewModel.loadFinish = true //显示FloatActionBar
                }
            })
        if (!viewModel.loadCourse) SelectionContainer {
            Text(
                text = viewModel.ics, modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )
            )
        }
    }

    mainViewModel.title = stringResource(id = R.string.Course)
    if (mainViewModel.loadFinish) mainViewModel.floaAction = {
        SmallFloatingActionButton(onClick = {
            if (permission.allPermissionsGranted) {
                val myFile = File("/data/data/com.jmu.assistant/files/course.ics")
                if (!myFile.exists()) myFile.createNewFile()
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.action = Intent.ACTION_VIEW
                intent.data =
                    FileProvider.getUriForFile(this, "com.jmu.assistant.fileprovider", myFile)
                myFile.writeText(viewModel.ics)
                viewModel.toast("请选择日历应用打开文件")
                this.startActivity(intent)
            } else permission.launchMultiplePermissionRequest()
        }) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Export")
        }
    }

    if (viewModel.loadCourse) ProgressDialog(stringResource(id = R.string.wait_loading))
}