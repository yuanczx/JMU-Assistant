package com.jmu.assistant.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.ImageCardList
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

