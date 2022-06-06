package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.dataStore
import com.jmu.assistant.entity.ContentNav

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun MainActivity.MainScreen() {
    /**
     * @Author yuanczx
     * @Description 主界面
     * @Date 2022/3/10 19:26
     * @Params []
     * @Return
     **/
    val mainNavController = rememberAnimatedNavController()
    LaunchedEffect(key1 = null, block = {
        mainViewModel.judgeStartRoute(dataStore)
        if (mainViewModel.startRoute == ContentNav.Login.route) {
            mainNavController.clearBackStack(ContentNav.Menu.route)
            mainNavController.navigate(mainViewModel.startRoute)
        }
    })
    Scaffold(Modifier.fillMaxSize()) {
        AnimatedNavHost(
            navController = mainNavController, startDestination = ContentNav.Menu.route
        ) {
            composable(ContentNav.Menu.route) {
                MenuScreen(mainNavController)
            }
            composable(ContentNav.Login.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }) {
                LoginScreen(mainNavController)
            }
            composable(ContentNav.Course.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }) {
                CourseScreen(mainNavController)
            }
            composable(ContentNav.Transcript.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }) {
                TranscriptScreen(mainNavController)
            }
            composable(
                route = ContentNav.Info.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }
            ) {
                InfoScreen(mainNavController)
            }
        }
    }
}