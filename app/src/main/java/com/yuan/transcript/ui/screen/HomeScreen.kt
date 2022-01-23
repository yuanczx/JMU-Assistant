package com.yuan.transcript.ui.screen

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yuan.transcript.entity.BtmNav
import com.yuan.transcript.viewmodel.HomeViewModel

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(cookieString: String, mainNavController: NavHostController) {
    val viewModel: HomeViewModel = viewModel()
    val navController = rememberNavController()
    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(text = "JMU Assistant") },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
    }, bottomBar = {
        BottomNavigation(
            backgroundColor = Color.White,
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
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text(text = stringResource(id = screen.stringId), fontSize = 12.sp) },
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
    }) {
        NavHost(
            navController = navController,
            startDestination = BtmNav.Func.route
        ) {
            composable(BtmNav.Func.route) {
                FuncScreen(mainNavController = mainNavController, cookieString)
            }
            composable(BtmNav.User.route) {
                UserScreen(mainNavController = mainNavController)
            }
        }
    }
}