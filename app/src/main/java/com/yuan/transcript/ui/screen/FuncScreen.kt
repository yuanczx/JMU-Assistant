package com.yuan.transcript.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuan.transcript.ui.widgets.AlertDialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.yuan.transcript.R
import com.yuan.transcript.entity.ContentNav
import com.yuan.transcript.ui.theme.Pink80
import com.yuan.transcript.ui.widgets.ProgressDialog
import com.yuan.transcript.viewmodel.FuncViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("SetJavaScriptEnabled", "PermissionLaunchedDuringComposition")
@Composable
fun FuncScreen(mainNavController: NavHostController, cookieString: String) {

    val viewModel: FuncViewModel = viewModel()
    viewModel.cookie = cookieString

    val permissionState = rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    if (viewModel.requirePermission)
    Log.d("ViewModel", viewModel.toString())
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp, 50.dp),
                    onClick = {
                        mainNavController.navigate(ContentNav.Course.route){
                            launchSingleTop = true
                        }
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_course),
                        contentDescription = "Course Table",
                        tint = Pink80
                    )
                }
                IconButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp, 50.dp),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_score),
                        contentDescription = "Transcript",
                        tint = Pink80
                    )
                }
            }
        }
    }
}

