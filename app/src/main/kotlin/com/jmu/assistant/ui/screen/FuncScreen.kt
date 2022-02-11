package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.ImageCardList
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
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

