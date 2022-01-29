package com.jmu.assistant

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.screen.*
import com.jmu.assistant.ui.theme.TranscriptTheme
import com.jmu.assistant.viewmodel.MainViewModel
import kotlinx.coroutines.launch


val MainActivity.dataStore by preferencesDataStore(name = "main")

class MainActivity : ComponentActivity() {
    companion object {
        var studentID: String = ""
        var cookie: String = ""
        val COOKIE_KEY = stringPreferencesKey("cookie")
    }

    val mainViewModel by viewModels<MainViewModel>()


    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.viewModelScope.launch {
            if (mainViewModel.judgeStartRoute(dataStore))
                mainViewModel.getStudentInfo()
        }
        setContent {
            var bottomBar by remember { mutableStateOf(false) }
            val navController = rememberAnimatedNavController()
            TranscriptTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = {
                        if (mainViewModel.showTopBar) {
                            SmallTopAppBar(
                                navigationIcon = mainViewModel.navigationIcon,
                                actions = mainViewModel.actions,
                                title = { Text(text = mainViewModel.title) },
                                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                                colors = TopAppBarDefaults.smallTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.background,
                                    navigationIconContentColor = MaterialTheme.colorScheme.background,
                                    actionIconContentColor = MaterialTheme.colorScheme.background
                                )
                            )
                        }
                    }, bottomBar = {
                        if (bottomBar) {
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary
                            ) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDes = navBackStackEntry?.destination
                                val items = listOf(BtmNav.Func, BtmNav.User)
                                items.forEach { screen ->
                                    BottomNavigationItem(alwaysShowLabel = false,
                                        selected = currentDes?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(id = screen.stringId),
                                                fontSize = 12.sp
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                modifier = Modifier.size(28.dp),
                                                painter = painterResource(id = screen.drawableId!!),
                                                contentDescription = stringResource(
                                                    id = screen.stringId
                                                ),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        })
                                }
                            }
                        }
                    }, floatingActionButton = mainViewModel.floatAction) {
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = mainViewModel.startRoute
                        ) {
                            composable(
                                ContentNav.Login.route,
                                exitTransition = { fadeOut(tween(0)) })
                            {
                                LoginScreen(navController)
                                bottomBar = false
                            }
                            composable(ContentNav.Transcript.route) {
                                TranscriptScreen()
                                mainViewModel.showTopBar = true
                                bottomBar = false
                            }
                            composable(ContentNav.Course.route, enterTransition = {
                                slideInHorizontally { fullWidth -> -fullWidth }
                            }, exitTransition = {
                                slideOutHorizontally{fullWidth -> -fullWidth }
                            }) {
                                bottomBar = false
                                mainViewModel.showTopBar = false
                                CourseScreen(navController)
                            }
                            composable(BtmNav.Func.route) {
                                FuncScreen(navController)
                                bottomBar = true
                                mainViewModel.showTopBar = true
                                mainViewModel.floatAction = {}
                            }
                            composable(BtmNav.User.route) {
                                UserScreen()
                                bottomBar = true
                                mainViewModel.floatAction = {}
                            }
                        }
                    }
                }
            }
        }
    }
}
