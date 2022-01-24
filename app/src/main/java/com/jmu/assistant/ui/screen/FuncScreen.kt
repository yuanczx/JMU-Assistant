package com.jmu.assistant.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.ImageButtonList
import com.jmu.assistant.viewmodel.FuncViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("SetJavaScriptEnabled", "PermissionLaunchedDuringComposition")
@Composable
fun FuncScreen(navController: NavHostController) {

    val viewModel: FuncViewModel = viewModel()

    val permissionState = rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    Log.d("ViewModel", viewModel.toString())
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val items = listOf(ContentNav.Course, ContentNav.Transcript)
                ImageButtonList(itemList = items, onClick = {
                    navController.navigate(it.route){
                        launchSingleTop = true
                    }
                })
            }
        }
    }
}

