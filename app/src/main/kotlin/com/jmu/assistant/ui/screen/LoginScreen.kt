package com.jmu.assistant.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.MainActivity.Companion.COOKIE_KEY
import com.jmu.assistant.MainActivity.Companion.cookie
import com.jmu.assistant.dataStore
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MainActivity.LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel()
    val scope = rememberCoroutineScope()

//    WebView 显示登录界面
    AndroidView(factory = {
        WebView(it).apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.blockNetworkImage = false
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    viewModel.url = request!!.url.toString()
                    return true
                }
            }
        }
    }, modifier = Modifier.fillMaxSize()) {
        it.loadUrl(viewModel.url)
    }


    LaunchedEffect(key1 = viewModel.url){
        if (viewModel.url == "http://jwxt.jmu.edu.cn/student/home") {
            cookie = CookieManager.getInstance().getCookie(viewModel.url)
            Log.d("cookie", cookie)
            scope.launch {
                //存储Cookie
                dataStore.edit {
                    it[COOKIE_KEY] = cookie
                }
                //获取StudentId
                mainViewModel.getStudentId()
                navController.navigate(BtmNav.Func.route) {
                    popUpTo(ContentNav.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}

