package com.jmu.assistant.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.ImageCardList

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("SetJavaScriptEnabled", "PermissionLaunchedDuringComposition")
@Composable
fun MainActivity.FuncScreen(navController: NavHostController) {
    mainViewModel.title = stringResource(id = R.string.app_name)
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
                ImageCardList(itemList = items, onClick = {
                    navController.navigate(it.route){
                        launchSingleTop = true
                    }
                })
            }
        }
    }
}

