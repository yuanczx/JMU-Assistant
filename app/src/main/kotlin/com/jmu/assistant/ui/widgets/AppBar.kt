package com.jmu.assistant.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.jmu.assistant.R

@ExperimentalMaterial3Api
@Composable
fun TopBar(
    @StringRes stringId: Int = R.string.app_name,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    /**
     * @Author yuanczx
     * @Description 顶部标题工具栏
     * @Date 2022/3/10 19:28
     * @Params [stringId, scrollBehavior, actions]
     * @Return
     **/
    SmallTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = navigationIcon,
        title = { Text(text = stringResource(stringId)) },
        scrollBehavior = scrollBehavior,
        actions = actions
    )
}

@Composable
fun BackIcon(navController: NavHostController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.back),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
