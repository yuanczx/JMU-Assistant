package com.jmu.assistant.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jmu.assistant.R

@ExperimentalMaterial3Api
@Composable
fun TopBar(
    @StringRes stringId: Int = R.string.app_name,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    SmallTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = stringResource(stringId)) },
        scrollBehavior = scrollBehavior,
        actions = actions
    )
}
