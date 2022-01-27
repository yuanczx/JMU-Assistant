package com.jmu.assistant.ui.screen

import android.annotation.SuppressLint
import android.webkit.*
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.MainActivity.Companion.cookie
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.viewmodel.LoginViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel()
    AndroidView(factory = {
        WebView(it).apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.blockNetworkImage = false
            settings.domStorageEnabled = true
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            settings.mixedContentMode =
                WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
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
    }, modifier = Modifier.height(400.dp)) {
        it.loadUrl(viewModel.url)
        if (viewModel.url == "http://jwxt.jmu.edu.cn/student/home") {
            cookie = CookieManager.getInstance().getCookie(viewModel.url)
            viewModel.getStudentID()
        }
    }


    LaunchedEffect(key1 = viewModel.login, block = {
        if (viewModel.login) navController.navigate(BtmNav.Func.route) {
            popUpTo(ContentNav.Login.route) {
                inclusive = true
            }
        }
    })
}

