package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.dataStore
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.IconDropMenu
import com.jmu.assistant.ui.widgets.ProgressDialog
import com.jmu.assistant.ui.widgets.TopBar
import com.jmu.assistant.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun MainActivity.MenuScreen(mainNavHostController: NavHostController) {

    /**
     * @Author yuanczx
     * @Description 菜单界面
     * @Date 2022/3/10 19:24
     * @Params [mainNavHostController]
     * @Return null
     **/
    val viewModel: MenuViewModel = viewModel()
    val navController = rememberAnimatedNavController()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = null, block = {
        viewModel.checkUpdate(false)
    })
    Scaffold(
        topBar = {
            TopBar(actions = {
                IconDropMenu(
                    painter = rememberVectorPainter(image = Icons.Default.MoreVert),
                    contentDescriptor = stringResource(R.string.menu_more),
                    list = listOf(
                        stringResource(id = R.string.relogin),
                        stringResource(id = R.string.check_update)
                    ),
                    onClick = {
                        when (it) {
                            0 -> scope.launch {
                                dataStore.edit { ds -> ds[MainActivity.COOKIE_KEY] = "" }
                                MainActivity.cookie = ""
                                mainNavHostController.navigate(ContentNav.Login.route) {
                                    launchSingleTop = true
                                    popUpTo(ContentNav.Menu.route) {
                                        inclusive = true
                                    }
                                }
                            }
                            1 -> {
                                viewModel.checkUpdate()
                            }
                        }
                    }
                )
            })
        },
        bottomBar = { BottomBar(navController = navController) }) {
        AnimatedNavHost(navController = navController, startDestination = BtmNav.Func.route) {
            composable(BtmNav.Func.route) {
                FuncScreen(navController = mainNavHostController)
            }
            composable(BtmNav.User.route) {
                UserScreen(viewModel = viewModel)
            }
        }
        if (viewModel.checkingUpdate) {
            ProgressDialog(stringResource(id = R.string.checking_update))
        }

        if (viewModel.newVersion) {
            com.jmu.assistant.ui.widgets.AlertDialog(
                title = stringResource(id = R.string.check_update),
                onConfirm = {
                    viewModel.update()
                },
                onDismiss = {
                    viewModel.newVersion = false
                }
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = stringResource(id = R.string.new_version) + "\n-----------------------------------")
                    Text(text = viewModel.versionName)
                    Text(text = viewModel.description)
                }
            }
        }
    }
}


@Composable
fun BottomBar(navController: NavHostController) {

    /**
     * @Author yuanczx
     * @Description 底部导航栏
     * @Date 2022/3/10 19:24
     * @Params [navController]
     * @Return null
     **/

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDes = navBackStackEntry?.destination
        val items = listOf(BtmNav.Func, BtmNav.User)
        items.forEach { screen ->
            NavigationBarItem(alwaysShowLabel = false,
                selected = currentDes?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = stringResource(id = screen.stringId))
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = screen.drawableId!!),
                        contentDescription = stringResource(id = screen.stringId),
                        tint = MaterialTheme.colorScheme.primary
                    )
                })
        }
    }

}
