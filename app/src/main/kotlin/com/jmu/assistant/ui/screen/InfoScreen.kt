package com.jmu.assistant.ui.screen

import android.webkit.WebView
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.R
import com.jmu.assistant.ui.widgets.BackIcon
import com.jmu.assistant.ui.widgets.TopBar
import com.jmu.assistant.viewmodel.InfoViewModel

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun MainActivity.InfoScreen(mainNavHostController: NavHostController) {

   val viewModel:InfoViewModel = viewModel()
    LaunchedEffect(key1 = null, block = {
        viewModel.getInfo(mainViewModel.articalLink)
    })
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(titleId = R.string.Info, navigationIcon = { BackIcon(navController = mainNavHostController)})
        AndroidView(modifier = Modifier.fillMaxSize(),factory = { WebView(it).apply {
        } }){
            if (viewModel.loadUrl){
                it.loadUrl(mainViewModel.articalLink)
            }else{
                it.loadData(viewModel.data,"text/html","utf-8")
            }
        }
    }
}