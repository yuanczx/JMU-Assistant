package com.jmu.assistant.ui.screen

import android.annotation.SuppressLint
import android.webkit.*
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jmu.assistant.MainActivity
import com.jmu.assistant.MainActivity.Companion.COOKIE_KEY
import com.jmu.assistant.MainActivity.Companion.cookie
import com.jmu.assistant.MainActivity.Companion.studentID
import com.jmu.assistant.dataStore
import com.jmu.assistant.entity.BtmNav
import com.jmu.assistant.entity.ContentNav
import com.jmu.assistant.utils.TheRetrofit
import com.jmu.assistant.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

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
    }, modifier = Modifier.height(400.dp)) {
        it.loadUrl(viewModel.url)
    }


    LaunchedEffect(key1 = viewModel.url){
        if (viewModel.url == "http://jwxt.jmu.edu.cn/student/home") {
            cookie = CookieManager.getInstance().getCookie(viewModel.url)
            scope.launch {
                //存储Cookie
                dataStore.edit {
                    it[COOKIE_KEY] = cookie
                }
                //获取StudentId
                val response = TheRetrofit.api.getStudentId().awaitResponse()
                val redirect = response.headers()["Location"] ?: ""
                studentID = redirect.substringAfter("/info/")
                navController.navigate(BtmNav.Func.route) {
                    popUpTo(ContentNav.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}

