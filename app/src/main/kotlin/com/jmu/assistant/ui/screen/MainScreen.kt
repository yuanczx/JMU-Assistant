package com.jmu.assistant.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.entity.ContentNav

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun MainActivity.MainScreen() {

    val mainNavController = rememberAnimatedNavController()
    Scaffold(Modifier.fillMaxSize()) {
        AnimatedNavHost(
            navController = mainNavController,
            startDestination = mainViewModel.startRoute
        ) {

            composable(ContentNav.Menu.route) {
                BottomNavScreen(mainNavController)
            }
            composable(
                ContentNav.Login.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }
            ) {
                LoginScreen(mainNavController)
            }
            composable(
                ContentNav.Course.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }
            ) {
                CourseScreen()
            }
            composable(
                ContentNav.Transcript.route,
                enterTransition = { slideInHorizontally { fullWidth -> -fullWidth } },
                exitTransition = { slideOutHorizontally { fullWidth -> -fullWidth } }
            ) {
                TranscriptScreen()
            }
        }
    }
}