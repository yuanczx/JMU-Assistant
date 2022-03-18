package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.ImageCardItem

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun FuncScreen(navController: NavHostController) {
    /**
     * @Author yuanczx
     * @Description 功能界面
     * @Date 2022/3/10 19:27
     * @Params [navController]
     * @Return
     **/
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
                val navItems = listOf(ContentNav.Course, ContentNav.Transcript)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(3.dp),
                    content = {
                        items(navItems) {
                            ImageCardItem(label = it.stringId, drawableRes = it.drawableId!!) {
                                navController.navigate(it.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    })
            }
        }
    }
}

