package com.jmu.assistant.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.ui.widgets.NewsItem
import com.jmu.assistant.viewmodel.MesseageViewModel

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun MainActivity.MesseageScreen(mainNavHostController: NavHostController) {
    /**
     * @Author yuanczx
     * @Description 信息界面
     * @Date 2022/3/21 9:18
     * @Params [mainNavHostController]
     * @Return
     **/
    val viewModel: MesseageViewModel = viewModel()

    val items = viewModel.getData().collectAsLazyPagingItems()
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = items.loadState.refresh == LoadState.Loading)

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
    ) {

        TabRow(
            selectedTabIndex = viewModel.selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[viewModel.selectedTabIndex]),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        ) {
            viewModel.msgList.forEachIndexed { index, s ->
                Tab(text = { Text(text = s) },
                    selected = viewModel.selectedTabIndex == index,
                    onClick = { viewModel.selectedTabIndex = index; })
            }
        }
        SwipeRefresh(state = swipeRefreshState, indicator = { s, trigger ->
            SwipeRefreshIndicator(s, trigger, contentColor = MaterialTheme.colorScheme.primary)
        }, onRefresh = items::refresh) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //主列表
                items(items) { news ->
                    news?.let {
                        NewsItem(news = it) { link ->
                            mainViewModel.articalLink = link
                            mainNavHostController.navigate(ContentNav.Info.route) {
                                popUpTo(mainNavHostController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
                //加载更多
                item {
                    if (items.loadState.append == LoadState.Loading) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            CircularProgressIndicator()
                            Divider(color = Color.Transparent, modifier = Modifier.width(10.dp))
                            Text(text = stringResource(id = R.string.wait_loading))
                        }
                    }
                }
            }
        }
    }
}